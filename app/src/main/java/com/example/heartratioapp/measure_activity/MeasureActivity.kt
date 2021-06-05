package com.example.heartratioapp.measure_activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import be.tarsos.dsp.AudioEvent
import be.tarsos.dsp.GainProcessor
import be.tarsos.dsp.filters.HighPass
import be.tarsos.dsp.filters.LowPassFS
import be.tarsos.dsp.io.TarsosDSPAudioFormat
import com.example.heartratioapp.R
import com.example.heartratioapp.diagnose_activity.ChartActivity
import com.example.heartratioapp.diagnose_activity.HistogramActivity
import com.example.heartratioapp.diagnose_activity.StatsActivity
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.*

class MeasureActivity : AppCompatActivity() {

    // VARIABLES
    var recordingThread: Thread? = null
    var isRecording = false

    var time_intervals: ArrayList<Double> =
        ArrayList()
    var maxInterval: Double? = null
    var minInterval: Double? = null
    var SDNN = 0.0
    var MSSD = 0.0
    var numbersOfIntervals = 0
    var mean = 0.0

    var btnRecord: Button? = null
    var btnStats:android.widget.Button? = null
    var btnHistogram:android.widget.Button? = null
    var btnPlot:android.widget.Button? = null
    var btnAnalyze:android.widget.Button? = null

    var text: TextView? = null
    var text2:TextView? = null

    private val RECORDER_SAMPLERATE = 44100
    private val RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO
    private val RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT

    var bufferSizeInBytes = AudioRecord.getMinBufferSize(
        RECORDER_SAMPLERATE,
        RECORDER_CHANNELS,
        RECORDER_AUDIO_ENCODING
    )
    var audioRecorder: AudioRecord? = null

    val REQUEST_AUDIO_PERMISSION_CODE = 1
    var Data = ByteArray(bufferSizeInBytes)
    lateinit var audioFloats: FloatArray
    lateinit var audioBytes: ByteArray
    var format: TarsosDSPAudioFormat? = null
    // END VARIABLES


    // METHODS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_measure)

        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                111
            )
        }


        text = findViewById<View>(R.id.textView) as TextView
        text2 = findViewById<View>(R.id.leftTime) as TextView
        text!!.text = "30"
        btnRecord = findViewById<View>(R.id.btnRecord) as Button
        btnStats = findViewById<View>(R.id.statistics) as Button
        btnHistogram = findViewById<View>(R.id.button) as Button
        btnPlot = findViewById<View>(R.id.wykres) as Button
        btnAnalyze = findViewById<View>(R.id.analyze) as Button

        audioRecorder = AudioRecord(
            MediaRecorder.AudioSource.CAMCORDER,
            RECORDER_SAMPLERATE, RECORDER_CHANNELS,
            RECORDER_AUDIO_ENCODING, bufferSizeInBytes
        )


        // TO DO
        btnStats!!.setOnClickListener {
            openActivityStats()
        }

        btnHistogram!!.setOnClickListener {
            openActivityHistogram()
        }

        btnPlot!!.setOnClickListener {
            openActivityPlot()
        }
        // END TO DO


        btnRecord!!.setOnClickListener {

            startRecording()
            Toast.makeText(
                applicationContext,
                "Rozpoczęto nagrywanie",
                Toast.LENGTH_LONG
            ).show()
            btnAnalyze!!.isEnabled = true

        }

        btnAnalyze!!.setOnClickListener {
            analyzePCM()
        }

    }


    fun openActivityStats() {
        val intent = Intent(this, StatsActivity::class.java)
        intent.putExtra("max", maxInterval)
        intent.putExtra("min", minInterval)
        intent.putExtra("SDNN", SDNN)
        intent.putExtra("MSSD", MSSD)
        intent.putExtra("numOfInt", numbersOfIntervals)
        intent.putExtra("mean", mean)
        startActivity(intent)
    }

    fun openActivityHistogram() {
        var array: IntArray? = IntArray(90)
        array = histogram(time_intervals)
        val intent = Intent(this, HistogramActivity::class.java)
        intent.putExtra("ARRAY", array)
        startActivity(intent)
    }


    fun openActivityPlot() {
        val len = audioFloats.size / 1000 + 100
        val audio = FloatArray(len)
        var licznik = 0
        for (i in audioFloats.indices) {
            if (i % 1000 == 0) {
                audio[licznik] = audioFloats[i]
                licznik++
            }
        }
        val intent = Intent(this, ChartActivity::class.java)
        intent.putExtra("floats", audio)
        startActivity(intent)
    }

    fun histogram(table: List<Double>): IntArray? {
        var counter = 0
        val start_window = 250
        val intArray = IntArray(90)
        var `val` = 0.0
        for (i in 0..89) {
            for (j in table.indices) {
                `val` = table[j]
                if (`val` >= start_window + 25 * i && `val` < start_window + 25 * (i + 1)) {
                    counter++
                }
            }
            intArray[i] = counter
            counter = 0
        }
        return intArray
    }



    // IMPORTANT
    fun analyzePCM() {

        format = TarsosDSPAudioFormat(
            RECORDER_SAMPLERATE.toFloat(),
            16,
            1,
            true,
            false
        )

        val sbuf =
            ByteBuffer.wrap(audioBytes).order(ByteOrder.LITTLE_ENDIAN)
                .asShortBuffer()

        val audioShorts = ShortArray(sbuf.capacity())

        sbuf[audioShorts]

        audioFloats = FloatArray(audioShorts.size)

        for (i in audioShorts.indices) {
            audioFloats[i] = audioShorts[i].toFloat() / 0x8000
        }

        val audioEvent = AudioEvent(format)
        audioEvent.floatBuffer = audioFloats

        val gainProcessor = GainProcessor(3.0)
        gainProcessor.process(audioEvent)

        val lowPassFilter = LowPassFS(200.toFloat(), RECORDER_SAMPLERATE.toFloat())
        lowPassFilter.process(audioEvent)

        val highPassFilter = HighPass(50.toFloat(), RECORDER_SAMPLERATE.toFloat())
        highPassFilter.process(audioEvent)

        var rms = AudioEvent.calculateRMS(audioEvent.floatBuffer)
        rms = 20.0 * Math.log10(rms)

        val window = 1024
        val samples = audioFloats.size
        val intervals = samples / window
        val war_rms = DoubleArray(intervals)

        for (i in 10 until intervals) {
            if (i * window > samples) {
                break
            }
            val buffer = FloatArray(window)
            for (j in 0 until window) {
                buffer[j] = audioFloats[i * window + j]
            }
            war_rms[i] = AudioEvent.calculateRMS(buffer)
            war_rms[i] = 20.0 * Math.log10(war_rms[i])
        }

        // 44100 probek to 1 sekunda
        // 1024 probki to okolo 23 ms
        // 50 BPM to uderzenia co 1200ms, czyli co okolo 51.68 (okienek) (min)
        // 180 BPM to uderzenia co 333ms, czyli co okolo 14.35 probek (max)
        var beats = 0
        val index: MutableList<Int> = ArrayList()

        // odfiltrowanie syfu poprzez zliczenie probek przekraczajacych srednie RMS,
        // oraz spelniajace warunek ze BPM nie przekroczy 180 BPM
        run {
            var i = 0
            while (i < war_rms.size) {
                if (war_rms[i] > rms) {
                    beats++
                    index.add(i)
                    i = i + 18
                }
                i++
            }
        }

        var x1: Double =
            java.lang.Double.valueOf(window.toDouble()) * java.lang.Double.valueOf(index[0].toDouble()) + 10 * intervals

        var x2 = 0.0

        var odstep = 0.0

        for (n in 1 until index.size) {
            x2 =
                java.lang.Double.valueOf(window.toDouble()) * java.lang.Double.valueOf(index[n].toDouble())
            odstep = x2 - x1
            odstep = 1000 * odstep / RECORDER_SAMPLERATE
            val ods = Math.round(odstep).toDouble()
            time_intervals.add(ods)
            x1 = x2
        }

        var sum = 0.0
        mean = 0.0

        for (i in time_intervals.indices) {
            sum += time_intervals[i]
        }

        mean = sum / time_intervals.size

        val BPM = Math.round(60000 / mean).toDouble()
        val s = BPM.toString()

        text!!.textSize = 24f
        text!!.text = "\nUderzenia $beats\n"
        text!!.append("BPM $s\n")

        maxInterval = CountMaxInterval(time_intervals)
        minInterval = CountMinInterval(time_intervals)

        SDNN = CountSDNN(time_intervals)
        MSSD = CountMSSD(time_intervals)

        numbersOfIntervals = CountMoreThan50(time_intervals)
        btnPlot!!.isEnabled = true
        btnHistogram!!.isEnabled = true
        btnStats!!.isEnabled = true
    }

    fun CountMaxInterval(table: List<Double>): Double {
        var max = table[0]
        for (i in 1 until table.size) {
            if (table[i] > max) {
                max = table[i]
            }
        }
        return max
    }

    fun CountMinInterval(table: List<Double>): Double {
        var min = table[0]
        for (i in 1 until table.size) {
            if (table[i] < min) {
                min = table[i]
            }
        }
        return min
    }

    fun CountSDNN(table: List<Double>): Double {
        var sum = 0.0
        var mean = 0.0
        var element = 0.0
        for (i in table.indices) {
            sum += table[i]
        }
        mean = sum / table.size
        sum = 0.0
        for (i in table.indices) {
            element = (mean - table[i]) * (mean - table[i])
            sum += element
        }
        sum = sum / table.size.toDouble()
        sum = Math.sqrt(sum)
        return sum
    }

    fun CountMSSD(table: List<Double>): Double {
        var element1 = 0.0
        var element2 = 0.0
        var sum = 0.0
        for (i in 0 until table.size - 1) {
            element1 = table[i]
            element2 = table[i + 1]
            sum += (element2 - element1) * (element2 - element1)
        }
        sum = sum / (table.size - 1).toDouble()
        sum = Math.sqrt(sum)
        return sum
    }

    fun CountMoreThan50(table: List<Double>): Int {
        var number = 0
        var e1 = 0.0
        var e2 = 0.0
        for (i in 0 until table.size - 1) {
            e1 = table[i]
            e2 = table[i + 1]
            if (Math.abs(e2 - e1) > 50) {
                number++
            }
        }
        return number
    }

    fun startRecording() {
        audioRecorder!!.startRecording()
        isRecording = true
        recordingThread = Thread(Runnable {
            var counter = 0
            val x = 1
            var turn_off = 0
            var left = 30
            var z: String
            val second: Int = RECORDER_SAMPLERATE / (bufferSizeInBytes / 2)
            val filepath = this.getExternalCacheDir().toString()
            var os: FileOutputStream? = null
            val baos = ByteArrayOutputStream()
            try {
                os = FileOutputStream("$filepath/record.pcm")
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            while (isRecording) {
                audioRecorder!!.read(Data, 0, Data.size)
                try {
                    os!!.write(Data, 0, bufferSizeInBytes)
                    baos.write(Data, 0, bufferSizeInBytes)
                    counter++
                    turn_off++
                    if (counter > second) {
                        if (turn_off > second * 30) {
                            left--
                            stopRecording()
                        }
                        left--
                        counter = 0
                        z = left.toString()
                        text!!.text = z
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            text2!!.text = "Kliknij przycisk: DOKONAJ ANALIZY aby zobaczyć wyniki!"
            text!!.text = ""
            try {
                os!!.close()
                audioBytes = baos.toByteArray()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        })
        recordingThread!!.start()
        try {
            Thread.sleep(0)
            Toast.makeText(applicationContext, "Rozpoczeto nagrywanie", Toast.LENGTH_LONG)
                .show()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    fun stopRecording() {
        if (audioRecorder != null) {
            isRecording = false
            audioRecorder!!.stop()
            audioRecorder!!.release()
            audioRecorder = null
            recordingThread = null
        }
    }


}


//gemoy
package rip.mapple.app

import android.os.Bundle
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        webView = WebView(this)
        setContentView(webView)

        // Pengaturan WebView
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true // Membantu PWA / penyimpanan lokal web
        }

        webView.webViewClient = object : WebViewClient() {
            // Menggunakan metode modern (Android 6.0+)
            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                // Hanya alihkan ke offline jika error terjadi pada halaman utama (bukan gambar/resource pendukung)
                if (request?.isForMainFrame == true) {
                    webView.loadUrl("file:///android_asset/offline.html")
                }
            }
        }

        // Menangani navigasi tombol Back sesuai rekomendasi AndroidX terbaru
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (::webView.isInitialized && webView.canGoBack()) {
                    webView.goBack()
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })

        webView.loadUrl("https://mapple.rip")
    }
}

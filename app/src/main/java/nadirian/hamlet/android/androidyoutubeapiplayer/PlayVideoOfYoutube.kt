package nadirian.hamlet.android.androidyoutubeapiplayer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView

class PlayVideoOfYoutube : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener,
    YouTubePlayer.PlayerStateChangeListener, YouTubePlayer.PlaybackEventListener {
    private lateinit var videoId: String
    lateinit var youTubePlayerView: YouTubePlayerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_video_of_youtube)
        var intent = intent
        videoId = intent.getStringExtra("videoid")!!
        youTubePlayerView = findViewById(R.id.youtubePlayer)
        youTubePlayerView.initialize("AIzaSyDoVdfda3x50zuLxU9n-zZunBn_elMKddc", this)
    }

    override fun onInitializationSuccess(
        p0: YouTubePlayer.Provider?,
        p1: YouTubePlayer?,
        p2: Boolean
    ) {
        p1?.setPlayerStateChangeListener(this)
        p1?.setPlaybackEventListener(this)
        if (!p2) {
            p1?.cueVideo(videoId)
        }

    }

    override fun onInitializationFailure(
        p0: YouTubePlayer.Provider?,
        p1: YouTubeInitializationResult?
    ) {
        Toast.makeText(this, "onInitializationFailure", Toast.LENGTH_SHORT).show()

    }

    override fun onLoading() {
    }

    override fun onLoaded(p0: String?) {
    }

    override fun onAdStarted() {
    }

    override fun onVideoStarted() {
    }

    override fun onVideoEnded() {
    }

    override fun onError(p0: YouTubePlayer.ErrorReason?) {
    }

    override fun onPlaying() {
    }

    override fun onPaused() {
    }

    override fun onStopped() {
    }

    override fun onBuffering(p0: Boolean) {
    }

    override fun onSeekTo(p0: Int) {
    }
}
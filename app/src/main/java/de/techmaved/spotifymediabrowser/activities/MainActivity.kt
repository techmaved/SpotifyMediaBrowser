package de.techmaved.spotifymediabrowser.activities

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.techmaved.spotifymediabrowser.components.Authentication
import de.techmaved.spotifymediabrowser.components.Database
import de.techmaved.spotifymediabrowser.components.MediaItems
import de.techmaved.spotifymediabrowser.components.SpotifyDesign
import de.techmaved.spotifymediabrowser.models.Model
import de.techmaved.spotifymediabrowser.ui.theme.SpotifyMediaBrowserTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val activity = this
            SpotifyMediaBrowserTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Ui(
                        activity,
                        Model.credentialStore.spotifyToken != null,
                        isPackageInstalled(SpotifyDesign().spotifyPackage, applicationContext.packageManager)
                    )
                }
            }
        }
    }

    private fun isPackageInstalled(packageName: String, packageManager: PackageManager): Boolean {
        return try {
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
}

@Composable()
fun Ui(activity: MainActivity?, isAuthenticated: Boolean, isSpotifyInstalled: Boolean) {
    val mediaItemCount = remember { mutableStateOf(0) }

    Column(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Authentication().SpotifyAuthSection(isAuthenticated, activity)
        Database().MediaItemsInDatabase(mediaItemCount)
        MediaItems().TextWithButtons(mediaItemCount, isAuthenticated)
        MediaItems().MirrorSection(isAuthenticated)
        SpotifyDesign().LinkToSpotify(isSpotifyInstalled, activity)
    }
}

@Composable
@Preview(showBackground = true)
fun Preview() {
    Ui(null, true, true)
}
package com.florintiron.githubrepobrowse

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.florintiron.githubrepobrowse.ui.theme.GithubRepoBrowseTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GithubRepoBrowseTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    SearchScreen(
                        showRepoDetails = { repoId ->
                            Toast.makeText(
                                this,
                                "Repository id: $repoId",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                    )
                }
            }
        }
    }
}
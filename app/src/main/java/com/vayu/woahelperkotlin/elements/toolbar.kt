package com.vayu.woahelperkotlin.elements

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import com.vayu.woahelperkotlin.R
import com.vayu.woahelperkotlin.ui.theme.WoaHelperKotlinTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vayu.woahelperkotlin.SettingsActivity
import com.vayu.woahelperkotlin.app

@Composable
fun toolbar(showIcons: Boolean) {
    val context = LocalContext.current
    WoaHelperKotlinTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = colorResource(R.color.toolbar))
        ) {
            Row {
                Image(
                    painter = painterResource(R.drawable.ic_launcher_foreground),
                    contentDescription = "App Icon",
                    modifier = Modifier
                        .size(70.dp)
                )
                Column (
                    modifier = Modifier
                        .padding(top = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.app_name),
                        color =  colorResource(R.color.white),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = stringResource(R.string.app_subtitle, app.version),
                        color =  colorResource(R.color.white),
                        style = MaterialTheme.typography.titleSmall
                    )
                }
                if (showIcons) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Image(
                            painter = painterResource(R.drawable.info),
                            contentDescription = "Info",
                            modifier = Modifier
                                .padding(end = 1.dp, top = 10.dp)
                                .size(50.dp)
                                .scale(1.25f)
                                .clickable {
                                    Toast.makeText(context, "Soon!", Toast.LENGTH_SHORT).show()
                                }
                        )
                        Image(
                            painter = painterResource(R.drawable.gear),
                            contentDescription = "Settings",
                            modifier = Modifier
                                .padding(end = 10.dp, top = 10.dp)
                                .size(50.dp)
                                .scale(1.35f)
                                .clickable {
                                    context.startActivity((Intent(context, SettingsActivity::class.java)))
                                }
                        )
                    }
                }
            }
        }
    }
}
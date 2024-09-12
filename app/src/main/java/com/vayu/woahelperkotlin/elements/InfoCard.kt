package com.vayu.woahelperkotlin.elements

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.structuralEqualityPolicy
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.topjohnwu.superuser.Shell
import com.topjohnwu.superuser.ShellUtils
import com.vayu.woahelperkotlin.MainActivity
import com.vayu.woahelperkotlin.R
import com.vayu.woahelperkotlin.functions
import com.vayu.woahelperkotlin.ui.theme.WoaHelperKotlinTheme

@Composable
fun InfoCard() {
    functions.checkRam(LocalContext.current)
    val context = LocalContext.current
    WoaHelperKotlinTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier
                .padding(top = 20.dp)
        ) {
            Row (
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(start = 5.dp, end = 0.dp)
            ) {
                var width = 165.dp
                var height = 200.dp
                var padding = 5.dp
                var picture = functions.DeterminePicture(LocalContext.current)
                if (picture == "vayu") Image(painter = painterResource(R.drawable.vayu), contentDescription = "Device Picture",
                    modifier = Modifier
                        .padding(start = padding)
                        .width(width)
                        .height(height))
                else if (picture == "nabu") Image(painter = painterResource(R.drawable.nabu), contentDescription = "Device Picture",
                    modifier = Modifier
                        .padding(start = padding)
                        .width(width)
                        .height(height))
                else if (picture == "raphael") Image(painter = painterResource(R.drawable.raphael), contentDescription = "Device Picture",
                    modifier = Modifier
                        .padding(start = padding)
                        .width(width)
                        .height(height))
                else Image(painter = painterResource(R.drawable.unknown), contentDescription = "Device Image",
                    modifier = Modifier
                        .padding(start = padding)
                        .width(width)
                        .height(height))

                Surface (
                    shape = MaterialTheme.shapes.large,
                    color = colorResource(R.color.cards),
                    modifier = Modifier
                        .height(height)
                        .width(width + 70.dp)
                        .padding(start = 7.dp, end = 10.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "WOA HELPER",
                            color =  colorResource(R.color.white),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(top = 10.dp)
                                .fillMaxWidth()
                        )
                        Text(
                            text = when (picture) {
                                "vayu" -> "POCO X3 PRO (VAYU)"
                                "nabu" -> "XIAOMI PAD 5 (NABU)"
                                "raphael" -> "XIAOMI 9T PRO (RAPHAEL)"
                                else -> "UNKNOWN"
                            },
                            color =  colorResource(R.color.white),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(top = 2.dp)
                                .fillMaxWidth()
                        )
                        Text(
                            text = stringResource(R.string.ram) + " ${functions.ramvalue} GB",
                            color =  colorResource(R.color.white),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .width(185.dp)
                        )
                        Text(
                            text = stringResource(R.string.panel) + " ${functions.checkPanel()}",
                            color =  colorResource(R.color.white),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .width(185.dp)
                        )
                        var check= if (functions.bootImgCheck) stringResource(R.string.yes) else stringResource(R.string.no)
                        Text(
                            text = stringResource(R.string.boot_imgInWindows, check),
                            color =  colorResource(R.color.white),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .width(185.dp)
                        )
                        if (functions.isAB) {
                            Text(
                                text = stringResource(R.string.activeSlot) +
                                    if (ShellUtils.fastCmd("getprop ro.boot.slot_suffix") == "_a") "A"
                                    else if (ShellUtils.fastCmd("getprop ro.boot.slot_suffix") == "_b") "B"
                                    else ""
                                ,
                                color = colorResource(R.color.white),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Start,
                                modifier = Modifier
                                    .padding(start = 10.dp)
                                    .width(185.dp)
                            )
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxHeight(),
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            val context = LocalContext.current
                            Button(
                                onClick = {
                                    val i = Intent(Intent.ACTION_VIEW, Uri.parse(functions.getLink("guide", context)))
                                    context.startActivity(i)
                                          },
                                modifier = Modifier
                                    .padding(start = 0.dp, end = 5.dp, bottom = 7.dp)
                                    .height(35.dp)
                            ) {
                                Text(text = "Guide")
                            }
                            Button(
                                onClick = {
                                    val i = Intent(Intent.ACTION_VIEW, Uri.parse(functions.getLink("group", context)))
                                    context.startActivity(i)
                                          },
                                modifier = Modifier
                                    .padding(bottom = 7.dp)
                                    .height(35.dp)
                            ) {
                                Text(text = "Group")
                            }
                        }
                    }
                }
            }
        }
    }
}
package com.vayu.woahelperkotlin.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vayu.woahelperkotlin.functions
import com.vayu.woahelperkotlin.ui.theme.WoaHelperKotlinTheme
import com.vayu.woahelperkotlin.R

@Composable
fun button(Icon: Int, Title: String, Subtitle: String, OnClick : () -> Unit, scaleMultiplier : Float) {
    WoaHelperKotlinTheme {
        Surface (
            color = colorResource(R.color.cards),
            onClick = OnClick,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .padding(top = 10.dp, start = 10.dp, end = 10.dp)
                .fillMaxWidth()
                .clickable{}
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(vertical = 8.dp)
            ) {
                Image(
                    painter = painterResource(Icon),
                    contentDescription = "Button Icon",
                    modifier = Modifier
                        .size(70.dp)
                        .padding(horizontal = 10.dp)
                        .scale(scaleMultiplier)
                )
                Column {
                    Text(
                        text = Title,
                        color = colorResource(R.color.white),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    Text(
                        text = Subtitle,
                        color = colorResource(R.color.white),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Medium,
                        fontStyle = FontStyle.Italic,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}
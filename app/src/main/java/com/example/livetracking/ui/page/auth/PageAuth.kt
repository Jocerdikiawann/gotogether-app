package com.example.livetracking.ui.page.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.livetracking.R
import com.example.livetracking.ui.component.topbar.BaseTopBar
import com.example.livetracking.ui.theme.LiveTrackingTheme
import com.example.livetracking.ui.theme.Primary
import com.example.livetracking.utils.from

data class AuthStateUI(
    val loading: Boolean = false,
    val error: Boolean = false,
    val errMsg: String = "Oops. Internal Server Error",
    val success:Boolean = false,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageAuth(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    onSignIn: () -> Unit = {},
    onBackStack: () -> Unit
) {
    val ctx = LocalContext.current
    Scaffold(
        topBar = {
            Column(modifier = modifier.fillMaxWidth()) {
                BaseTopBar(context = ctx, title = "Sign In") {
                    onBackStack()
                }
            }

        },
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            contentPadding = PaddingValues(horizontal = 10.dp.from(ctx)),
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            content = {
                item {
                    Image(
                        painter = painterResource(id = R.drawable.ic_travelers),
                        contentDescription = "ic_travelers",
                        modifier = Modifier.size(200.dp.from(ctx))
                    )
                    Spacer(modifier = Modifier.height(10.dp.from(ctx)))
                    Text(
                        text = "Driving Becomes More Fun\nBy Sharing Your Journey Through Our App",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 16.sp.from(ctx),
                            fontWeight = FontWeight.Normal,
                        ),
                        textAlign = TextAlign.Center
                    )
                }
                item { Spacer(modifier = Modifier.height(10.dp.from(ctx))) }
                item {
                    OutlinedButton(
                        onClick = {
                            if (loading) Unit else onSignIn()
                        },
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.White,
                        ),
                        border = BorderStroke(1.dp, Color.Gray),
                        shape = RoundedCornerShape(8.dp.from(ctx)),
                    ) {
                        if (loading) {
                            CircularProgressIndicator(color = Primary)
                        } else {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_google),
                                    contentDescription = "ic_google",
                                    tint = Color.Unspecified,
                                    modifier = Modifier
                                        .height(25.dp.from(ctx))
                                        .width(25.dp.from(ctx))
                                )
                                Spacer(modifier = Modifier.width(10.dp.from(ctx)))
                                Text(
                                    text = "Continue with Google",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 14.sp.from(ctx),
                                        fontWeight = FontWeight.Normal,
                                        color = Color.Black
                                    )
                                )
                            }
                        }
                    }
                }
            })
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewPageAuth() {
    LiveTrackingTheme {
        PageAuth {}
    }
}
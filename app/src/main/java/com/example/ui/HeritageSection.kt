package com.example.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.Marigold
import com.example.ui.theme.Saffron
import com.example.ui.theme.Marigold

@Composable
fun HeritageSectionLocalized(viewModel: DevaYatraViewModel) {
    val isHi = viewModel.currentLanguage.collectAsState().value == "hi"
    val context = LocalContext.current
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Section header - clean, minimalist and sacred
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "ॐ",
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isHi) "बाबा धाम सहायता" else "Baba Dham Support",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Saffron
            )
        }

        // Row of 2 simple, highly-intuitive visual cards for absolute simplicity
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // 1. Interactive direct Helpline Card
            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        IntentUtils.dialPhoneNumber(context, "+919471155667")
                    },
                colors = CardDefaults.cardColors(
                    containerColor = Saffron.copy(alpha = 0.05f)
                ),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Saffron.copy(alpha = 0.25f))
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Saffron,
                        contentColor = Color.White,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Phone,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = if (isHi) "24/7 हेल्पलाइन" else "24/7 Helpline",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = Saffron,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = if (isHi) "तुरंत कॉल करें" else "Tap to Call",
                        style = MaterialTheme.typography.bodySmall,
                        color = com.example.ui.theme.BhasmDark,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // 2. Beautiful Simple Darshan Tip Card
            Card(
                modifier = Modifier
                    .weight(1f),
                colors = CardDefaults.cardColors(
                    containerColor = Marigold.copy(alpha = 0.04f)
                ),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Marigold.copy(alpha = 0.15f))
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Marigold,
                        contentColor = Color.White,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = if (isHi) "बाबा दर्शन" else "Baba Darshan",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = Marigold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = if (isHi) "जलार्पण मार्ग" else "Jalarpan Marg",
                        style = MaterialTheme.typography.bodySmall,
                        color = com.example.ui.theme.BhasmDark,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

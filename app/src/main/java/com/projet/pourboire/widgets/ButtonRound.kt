package com.projet.pourboire.widgets


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val IconSizeModifier = Modifier.size(40.dp)

@Composable
fun ButtonRound(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    onClick : ()-> Unit,
    tint: Color =Color.Black.copy(alpha = 0.8f),
    backroudColor: Color = MaterialTheme.colors.background,
    elevation : Dp = 4.dp
){
  Card(
      modifier = modifier
        .padding(all = 4.dp)
        .clickable { onClick.invoke() }.then(IconSizeModifier),
      shape = CircleShape,
      backgroundColor = backroudColor,
      elevation = elevation) {
      Icon(
          imageVector=imageVector,
          contentDescription = "Plus or minus icon",
          tint=tint)

  }  
}
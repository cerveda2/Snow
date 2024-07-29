package cz.dcervenka.snow.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SearchTextField(
    textState: TextFieldValue,
    favoriteToggled: Boolean,
    onFavoriteToggled: () -> Unit,
    onTextChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = textState,
        onValueChange = onTextChange,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        placeholder = { Text(text = "Search") },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = null)
        },
        trailingIcon = {
            IconButton(onClick = {
                onFavoriteToggled()
            }) {
                Icon(
                    imageVector = if (favoriteToggled) Icons.Filled.Favorite
                    else Icons.Filled.FavoriteBorder,
                    contentDescription = null
                )
            }
        },
        colors = OutlinedTextFieldDefaults.colors(),
        shape = CircleShape
    )
}

@Preview
@Composable
private fun SearchTextFieldPreview() {
    SearchTextField(
        textState = TextFieldValue("Hello World"),
        favoriteToggled = false,
        onFavoriteToggled = {},
        onTextChange = {}
    )
}
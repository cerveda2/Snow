package cz.dcervenka.snow.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cz.dcervenka.snow.R

@Composable
fun SearchTextField(
    textState: TextFieldValue,
    favoriteToggled: Boolean,
    onFavoriteToggled: () -> Unit,
    onTextChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isFocused by remember { mutableStateOf(false) }
    val focusManager: FocusManager = LocalFocusManager.current

    OutlinedTextField(
        value = textState,
        onValueChange = onTextChange,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            },
        placeholder = { Text(text = stringResource(R.string.search_resort_hint)) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        trailingIcon = {
            if (isFocused || textState.text.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Clear",
                    modifier = Modifier.clickable {
                        onTextChange(TextFieldValue(""))
                        focusManager.clearFocus()
                    }
                )
            } else {
                IconButton(onClick = {
                    onFavoriteToggled()
                }) {
                    Icon(
                        imageVector = if (favoriteToggled) Icons.Filled.Favorite
                        else Icons.Filled.FavoriteBorder,
                        contentDescription = null
                    )
                }
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary
        ),
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
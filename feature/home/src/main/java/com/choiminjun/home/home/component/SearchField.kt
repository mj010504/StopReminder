package com.choiminjun.home.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.choiminjun.designsystem.theme.SRTheme
import com.choiminjun.designsystem.theme.Shape
import com.choiminjun.home.R
import com.choiminjun.designsystem.R as DesignSystemR

@Composable
internal fun SearchField(
    value: String,
    onValueChange: (String) -> Unit,
    onFocused: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    var isFocused by remember { mutableStateOf(false) }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = SRTheme.colors.coolNeutral95,
                shape = Shape.sm,
            )
            .padding(horizontal = 12.dp, vertical = 12.dp)
            .onFocusChanged { focusState ->
                if (focusState.isFocused && !isFocused) {
                    isFocused = true
                    onFocused()
                } else if (!focusState.isFocused) {
                    isFocused = false
                }
            },
        textStyle = SRTheme.typography.bodyXMM,
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = { focusManager.clearFocus() },
        ),
        decorationBox = { innerTextField ->
            if (value.isEmpty() && !isFocused) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(DesignSystemR.drawable.ic_search),
                        contentDescription = null,
                        tint = SRTheme.colors.textSecondary,
                        modifier = Modifier.size(24.dp),
                    )
                    Text(
                        text = stringResource(R.string.search_hint),
                        style = SRTheme.typography.bodyXMR,
                        color = SRTheme.colors.textSecondary,
                    )
                }
            }
            innerTextField()
        },
    )
}

@Preview
@Composable
private fun SearchFieldPreview() {
    SearchField(value = "", onValueChange = {}, onFocused = {})
}

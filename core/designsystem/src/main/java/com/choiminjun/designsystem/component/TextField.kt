package com.choiminjun.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.choiminjun.designsystem.theme.CoolNeutral10
import com.choiminjun.designsystem.theme.CoolNeutral50
import com.choiminjun.designsystem.theme.CoolNeutral96
import com.choiminjun.designsystem.theme.SemanticNegative
import com.choiminjun.designsystem.theme.SemanticPrimary
import com.choiminjun.designsystem.theme.SemanticStrokeDefault
import com.choiminjun.designsystem.theme.Shape

@Composable
fun SRTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    helperText: String? = null,
    isError: Boolean = false,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    Column(modifier = modifier) {
        if (label != null) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = CoolNeutral10,
            )
            Spacer(modifier = Modifier.height(6.dp))
        }

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            textStyle = MaterialTheme.typography.bodyLarge,
            placeholder = if (placeholder != null) {
                { Text(text = placeholder, color = CoolNeutral50) }
            } else {
                null
            },
            isError = isError,
            shape = Shape.md,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = SemanticPrimary,
                unfocusedBorderColor = SemanticStrokeDefault,
                errorBorderColor = SemanticNegative,
                focusedTextColor = CoolNeutral10,
                unfocusedTextColor = CoolNeutral10,
                cursorColor = SemanticPrimary,
                errorCursorColor = SemanticNegative,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
            ),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = true,
        )

        if (helperText != null) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = helperText,
                style = MaterialTheme.typography.bodySmall,
                color = if (isError) SemanticNegative else CoolNeutral50,
            )
        }
    }
}

@Composable
fun SRSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "검색",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(Shape.md)
            .background(CoolNeutral96)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = CoolNeutral50,
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(start = 10.dp),
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = CoolNeutral10),
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.bodyLarge,
                    color = CoolNeutral50,
                )
            }
        }
    }
}

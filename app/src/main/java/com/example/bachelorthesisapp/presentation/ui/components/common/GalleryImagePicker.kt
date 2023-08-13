package com.example.bachelorthesisapp.presentation.ui.components.common

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.util.Base64
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bachelorthesisapp.R
import com.example.bachelorthesisapp.data.authentication.await
import com.example.bachelorthesisapp.presentation.ui.theme.Typography
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream

@Preview(showSystemUi = true, showBackground = true)
@SuppressLint("MutableCollectionMutableState")
@Composable
fun GalleryImagePicker(
    postId: Int = 0,
    postName: String = "",
    onValueChanged: (String) -> Unit = {}
) {

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val urisList by remember {
        mutableStateOf<MutableList<Uri?>>(
            mutableListOf(
            )
        )
    }
    val bitmap = remember {
        mutableStateOf<Bitmap?>(null)
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
        Log.d("URI", "$uri")
        if (imageUri !in urisList) {
            imageUri?.let { imgUri ->
                urisList.add(imgUri)
                val imagesString = urisList.joinToString(";") { it.toString() }
                onValueChanged(imagesString)
            }
        }
    }

    Column(
        horizontalAlignment = Alignment.Start, modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Row(
            modifier = Modifier.padding(start = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                modifier = Modifier,
                text = stringResource(R.string.Images),
                color = Color.DarkGray,
                style = Typography.subtitle1
            )
            Spacer(modifier = Modifier.width(20.dp))
            SmallSubmitButton(
                onClick = {
                    launcher.launch("image/*")
                },
                text = stringResource(id = R.string.Upload)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(3.dp),
            state = rememberLazyListState(initialFirstVisibleItemIndex = 0)
        ) {
            Log.d("IMAGES", urisList.toString())
            items(urisList.size) { imageIndex ->
                val imgUri = urisList[imageIndex]
                imgUri?.let {
                    val source = ImageDecoder
                        .createSource(context.contentResolver, it)
                    bitmap.value = ImageDecoder.decodeBitmap(source)
                    bitmap.value?.let { btm ->
                        Image(
                            bitmap = btm.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier
                                .size(200.dp)
                                .padding(5.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
    }

}

package com.example.wapp.demo.viewmodel

import android.content.ContentResolver
import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.Utils
import com.example.oapp.base.BaseViewModel
import com.example.wapp.demo.MyApp
import com.example.wapp.demo.bean.ListDataUiState
import com.hyphenate.easeui.model.VideoEntity
import com.hyphenate.easeui.utils.EaseCompat
import com.hyphenate.util.PathUtil
import com.hyphenate.util.VersionUtils
import java.io.File
import java.io.IOException
import java.util.*

/**
 * Created by jsxiaoshui on 2021-12-01
 */
class ImageGridViewModel:BaseViewModel() {


  public var videoLiveData: MutableLiveData<ListDataUiState<VideoEntity>> = MutableLiveData()
    /**
     * 获取视频列表
     * @param callBack
     */
    fun getVideoList() {
        if (!isLoggedIn()) {
            ToastUtils.showShort("请先登录~")
            return
        }
        requestHX(
            block = {
                val mList: MutableList<VideoEntity> = ArrayList()
                val mContentResolver: ContentResolver = MyApp.instance.contentResolver
                var cursor = mContentResolver.query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, null
                )
                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        // ID:MediaStore.Audio.Media._ID
                        val id = cursor.getInt(
                            cursor
                                .getColumnIndexOrThrow(MediaStore.Video.Media._ID)
                        )

                        // title：MediaStore.Audio.Media.TITLE
                        val title = cursor.getString(
                            cursor
                                .getColumnIndexOrThrow(MediaStore.Video.Media.TITLE)
                        )
                        // path：MediaStore.Audio.Media.DATA
                        var url: String? = null
                        if (!VersionUtils.isTargetQ(MyApp.instance)) {
                            url = cursor.getString(
                                cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
                            )
                        }

                        // duration：MediaStore.Audio.Media.DURATION
                        val duration = cursor
                            .getInt(
                                cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
                            )

                        // 大小：MediaStore.Audio.Media.SIZE
                        val size = cursor.getLong(
                            cursor
                                .getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
                        ).toInt()

                        // 最近一次修改时间：MediaStore.Audio.DATE_MODIFIED
                        val lastModified = cursor.getLong(
                            cursor
                                .getColumnIndexOrThrow(MediaStore.Video.Media.DATE_TAKEN)
                        )
                        if (size <= 0) {
                            continue
                        }
                        val uri =
                            Uri.parse(MediaStore.Video.Media.EXTERNAL_CONTENT_URI.toString() + File.separator + id)
                        val entty = VideoEntity()
                        entty.ID = id
                        entty.title = title
                        entty.filePath = url
                        entty.duration = duration
                        entty.size = size
                        entty.uri = uri
                        entty.lastModified = lastModified
                        mList.add(entty)
                    } while (cursor.moveToNext())
                }
                if (cursor != null) {
                    cursor.close()
                    cursor = null
                }
                getSelfVideoFiles( MyApp.instance, mList)

                if (mList.isNotEmpty()) {
                    sortVideoEntities(mList)
                }
                var tempList= arrayListOf<VideoEntity>()
                mList.forEach {
                    tempList.add(it)
                }
                val listDataUiState=ListDataUiState(
                    isSuccess = true,
                    listData = tempList
                )
                Utils.runOnUiThread{
                    videoLiveData.value=listDataUiState
                }
            }
        )
    }


    private fun getSelfVideoFiles(context: Context, mList: MutableList<VideoEntity>) {
        val videoFolder = PathUtil.getInstance().videoPath
        if (videoFolder.exists() && videoFolder.isDirectory) {
            val files = videoFolder.listFiles()
            if (files != null && files.size > 0) {
                var entty: VideoEntity
                for (i in files.indices) {
                    entty = VideoEntity()
                    val file = files[i]
                    if (!EaseCompat.isVideoFile(context, file.name) || file.length() <= 0) {
                        continue
                    }
                    entty.filePath = file.absolutePath
                    entty.size = file.length().toInt()
                    entty.title = file.name
                    entty.lastModified = file.lastModified()
                    val player = MediaPlayer()
                    try {
                        player.setDataSource(file.path)
                        player.prepare()
                        entty.duration = player.duration
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    if (entty.size <= 0 || entty.duration <= 0) {
                        continue
                    }
                    mList.add(entty)
                }
            }
        }
    }

    private fun sortVideoEntities(mList: List<VideoEntity>) {
        Collections.sort(mList) { o1, o2 ->
            (o2.lastModified - o1.lastModified).toInt()
        }
    }
}
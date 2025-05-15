package me.hgj.jetpackmvvm.demo.data.model.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable


data class ShareResponse(
    var coinInfo: CoinInfoResponse,
    var shareArticles: ApiPagerResponse<ArrayList<AriticleResponse>>
) : Serializable
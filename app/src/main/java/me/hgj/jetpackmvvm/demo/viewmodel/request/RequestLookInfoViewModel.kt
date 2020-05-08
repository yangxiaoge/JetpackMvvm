package me.hgj.jetpackmvvm.demo.viewmodel.request

import android.app.Application
import androidx.lifecycle.MutableLiveData
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.demo.app.network.stateCallback.ListDataUiState
import me.hgj.jetpackmvvm.demo.data.model.bean.AriticleResponse
import me.hgj.jetpackmvvm.demo.data.model.bean.ShareResponse
import me.hgj.jetpackmvvm.demo.data.repository.request.HttpRequestManger
import me.hgj.jetpackmvvm.ext.request

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/4
 * 描述　:
 */
class RequestLookInfoViewModel(application: Application) : BaseViewModel(application){

    var pageNo = 1

    var shareListDataUistate = MutableLiveData<ListDataUiState<AriticleResponse>>()

    var shareResponse  = MutableLiveData<ShareResponse>()

    fun getLookinfo(id: Int, isRefresh: Boolean) {
        if (isRefresh) {
            pageNo = 1
        }
        request({ HttpRequestManger.instance.getLookinfoById(id, pageNo) }, {
            //请求成功
            pageNo++
            shareResponse.postValue(it)
            val listDataUiState =
                ListDataUiState(
                    isSuccess = true,
                    isRefresh = it.shareArticles.isRefresh(),
                    isEmpty = it.shareArticles.isEmpty(),
                    hasMore = it.shareArticles.hasMore(),
                    isFirstEmpty = isRefresh && it.shareArticles.isEmpty(),
                    listData = it.shareArticles.datas
                )
            shareListDataUistate.postValue(listDataUiState)
        }, {
            //请求失败
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errorMsg,
                    isRefresh = isRefresh,
                    listData = arrayListOf<AriticleResponse>()
                )
            shareListDataUistate.postValue(listDataUiState)
        })
    }
}
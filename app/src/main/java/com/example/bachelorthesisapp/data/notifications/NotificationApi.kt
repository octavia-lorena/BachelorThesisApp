package com.example.bachelorthesisapp.data.notifications

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationApi {
    companion object {

        const val BASE_URL = "https://fcm.googleapis.com"
        const val SERVER_KEY =
            "AAAAGhQSoO8:APA91bGODM2ctiMN-ipGOvJeA1w_GAmy3pfzOWIVEhTAKY0a-BD4U4-nqXTEJZFkJTKCI6KZpkNEQnwY-QQ8-EpS0Nhg3QLf8TI7lHjwTF3y7oy1wnpDaESeZ7ODsvz-LEVSu-bs0W0Y"
//            "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCVR9HdL/d/5LaO\nQub7PFf6l9EWKhJhgQjG72PVmTIRIKjvcdgZky+OUHFbvs5/M7Pz3aT9aLKxTpmx\nvzHrZg5GhI8RLTAlCcs/cWQLCRUYZjvf977T2nJ+hIR5U2IcoIS8/V3br+xHEtWM\nzGv8UzTWxGxdaFnrQRNQQIUITJIF8YtHIFM8M0B5SY9ynjuyH4nR/h6o5bsF5rRG\nw/5kJ4avkQh/PRDdzUDJARhDZAD3eym/cOF6DnpxeyAQGE17HeoZVnmaLr4HaoEE\nO/f16/HhArKHJPJjFcHvt/GUav9UpVQuTplPwj5csoYUsCWlVWYGMNyKvRfo7Lys\nA69chjv9AgMBAAECggEAEuWMg1tXETF6inedlaAF4wrkxNF+kikrW0Nid990nwZr\nXCQlHteq5XD5aIu2yBNbbDtab1mZhShV/FIfl5TLqdGN6ra1qXqb5+QKrfrwWK5Y\nVfmehmLYq3tK6bDMcqM/Kk1IXE7gsf6IWdJmuQIvZiBnN0vGVilesIH6VRqcQXQo\njKGrFnOGJcc2glpfA4dPVXCGjVzxUqpsah6IKu6AHE1n9+7iGpvhL+SEgSyXbgJ3\ntP0Az1rSGJud+4zm4pH7zfkWL2Ow3kfCsppEJzzNT++o9xumbyuQf/4RuuRc7hXy\nUU5N6/ijo2stjhvN/fFeemdgpSKQMOL7eVjblSf3AQKBgQDLo8rGKG1KRdelF2E0\noMqUm8hVhkT8CMebyM7jVfvDMSnFLe1ybpRGfW9IdEfj2Rc5M7idufNdDpj+3AsE\nBNHJVfiaceGrHQ8NtM6+yX/rUgvitBa4VR9K6UeWgzPkHAERsvnSs21wxgcIK9qe\nmxIB+qAqvTmUfGNvc1l6x4ZbBQKBgQC7qe5eqansiHLFRzcPhqUZ2SVejeghgBd4\noSsZSDm9urcdoRWK4tFsekxOhcG9/rulwx6I/DqrOnfGIq6pgl3z1bsPCOQl3QHO\n0xmS/IAg06+vxRayMPS/P0MgSJJzxUQ3ZVdEsF8u+bMdWGAjvv0k+lnL1NIZaXnc\nhFGs3fZemQKBgEwBpXSSsEARk2x2LnzjKtmOz5ZEWEktybr2l2m4tRBksC99devJ\nJCSvOrLzW8XKSGW8TULpndD02tv9K0tjtR2Nrg2PReiFHxJBkw0Yzlr5s5/5EaXZ\nd6WRLnwaGmsgnLGLeL1Q5R87enJtU9rxDCaa8d8h43TtfHujtRkjkZ01AoGABK3N\n9apBRFZ6lhSaeNagky5o0hyzpvIso6NREcZNOj/HhQOZJ+dT1WKPOH35QKVzRETD\nJCM1y0Mf7V68ABnyAbxYd51/IrqJ6wA0aQfGogrlHHrLgBHcaFBJeiqVDDHk64Sp\nUlsbZG5xWs/zRbDjWuKrDnv16RBKK4dZCfhWxjECgYEAmkXQtqWHFVw3/wdQ731c\n54cCTVdTf6Xx+oXPyQHi3ZKbmKG2PoRKicQmyR4hrfv/SduvRPkgRD0DFhrCsv1r\nt/LlvxKQXPx4OIgqqo/exj4CcmrJGxhwOpHQLUGypIjiUxEmUrP02KZRTMpkqn4k\n26YTSKYrHYdRG4nasI2SFfo="

        // const val SERVER_KEY = "103205203169558740902"
        const val CONTENT_TYPE = "application/json"
    }

    @Headers("Authorization: key=$SERVER_KEY", "Content-type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification: PushNotification
    ): Response<ResponseBody>
}
package com.imlibrary.login;

import android.text.TextUtils;

import com.imlibrary.constant.TCConstants;
import com.tencent.TIMCallBack;
import com.tencent.TIMManager;
import com.tencent.TIMUser;


/**
 * Created by RTMP on 2016/8/3
 * IM登录模块
 */
public class TCLoginMgr {


    private TCLoginMgr() {
    }

    private static class TCLoginMgrHolder {
        private static TCLoginMgr instance = new TCLoginMgr();
    }

    public static TCLoginMgr getInstance() {
        return TCLoginMgrHolder.instance;
    }


    /*********************************************************************************************************************
     *   登录模块介绍：
     *   TLS登录支持三种模式：1.托管模式 2.游客模式 3.独立模式
     *
     *   1.托管模式：
     *     1.1) 用户名密码等账号信息托管在腾讯后台，这是我们在苹果AppStore上的APP所采用的模式
     *     1.2) 调用TLS(tencent login service)获取TLSUserInfo,内部包含了短期有效的id和签名，这段逻辑详见 TCLoginActivity.java
     *     1.3) 使用TLS登录返回的id和签名，调用TCLoginMgr的imLogin()函数完成IM模块的登录，之后就可以收发消息了
     *
     *   2.游客模式：
     *     2.1) 如果您已有帐号体系，适合使用这种模式，该模式下腾讯云通讯模块会使用内部的一些匿名账号进行消息的收发。
     *     2.2) 这种模式下，您只需要调用TCLoginMgr的guestLogin()函数即可实现，内部流程跟托管模式类似，只是账号换成了随机生成的匿名账号。
     *     2.3) 如果想要将IM模块跟您的账号体系进行结合，实现您的两个账号间的私信收发，请看独立模式。
     *
     *   3.独立模式
     *     3.1) 如果您已有帐号体系且需要支持可靠的C2C消息，则需要使用此种方式。
     *          此模式的目标是将腾讯云通讯模块跟您的账号体系结合起来实现安全可靠的消息通讯。
     *     3.2) 您先使用自己的登录逻辑进行登录，之后交给您的服务器验证当前用户是不是一个合法的用户。
     *     3.3) 如果当前用户通过验证，您的登录服务器要使用跟腾讯云协商的非对称加密密钥对用户ID进行签名。
     *          这就好比您的服务器给这个用户ID做了担保：“这是个好孩子，我给他担保，有公章在此，请给他通过。”
     *     3.4) APP在收到用户ID和签名后，TCLoginMgr的imLogin()函数完成IM模块的登录，之后就可以收发消息了
     *
     ********************************************************************************************************************
     *
     *    如下是本Class的全流程代码逻辑介绍：
     *
     *      1.注册登录回调(TCLoginMgr#setTCLoginCallback(TCLoginCallback))
     *
     *      2.获取用户ID与PWD根据ID与PWD生成sig
     *             独立模式：您的后台服务器负责用户登录验证，并对登录成功的用户生成UserSig签名(需提前于云通信后台配置签名用的非对称密钥)
     *             托管模式：获取userid以及password后直接调用pwdLogin(String, String)函数完成登录，跳过步骤（3）
     *             游客模式：直接调用TCLoginMgr的guestLogin()函数完成登录，跳过步骤（3）
     *
     *      3.通过鉴权返回的ID与Sig进行登录(TCLoginMgr#imLogin(String UserId, String UserSig))
     *
     *      4.Login回调获取相应信息后，移除回调(TCLoginMgr#removeIMLoginCallback())避免内存泄漏
     *
     *      5.需要退出登录时，调用接口登出(TCLoginMgr#imLogout())
     *
     * *******************************************************************************************************************/

    //登录回调
    private TCLoginCallback mTCLoginCallback;


    /**
     * Login回调
     */
    public interface TCLoginCallback {

        /**
         * 登录成功
         */
        void onSuccess();

        /**
         * 登录失败
         *
         * @param code 错误码
         * @param msg  错误信息
         */
        void onFailure(int code, String msg);

    }


    public void setTCLoginCallback(TCLoginCallback tcLoginCallback) {
        this.mTCLoginCallback = tcLoginCallback;
    }

    public void removeTCLoginCallback() {
        this.mTCLoginCallback = null;
    }


    /**
     * 检查是否存在缓存，若存在则登录，反之回调onFailure
     */
    public boolean checkCacheAndLogin() {
        imLogin(TCConstants.getIdentify(), TCConstants.getUserSig(), TCConstants.getAppid(), TCConstants.getType());
        return true;
    }


    /**
     * imsdk登录接口，与tls登录验证成功后调用
     *
     * @param identify 用户id
     * @param userSig  用户签名（托管模式下由TLSSDK生成 独立模式下由开发者在IMSDK云通信后台确定加密秘钥）
     */
    public void imLogin(String identify, String userSig, String appid, String type) {
        if (TextUtils.isEmpty(identify) || TextUtils.isEmpty(userSig) || TextUtils.isEmpty(appid.trim()) || TextUtils.isEmpty(type))
            return;
        TCConstants.setAppid(appid.trim());
        TCConstants.setIdentify(identify);
        TCConstants.setType(type);
        TCConstants.setUserSig(userSig);


        TIMUser user = new TIMUser();
        user.setAccountType(String.valueOf(type));
        user.setAppIdAt3rd(String.valueOf(appid.trim()));
        user.setIdentifier(identify);
        //发起登录请求
        TIMManager.getInstance().login(Integer.parseInt(appid.trim()), user, userSig, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                if (null != mTCLoginCallback)
                    mTCLoginCallback.onFailure(i, s);
            }

            @Override
            public void onSuccess() {
                if (null != mTCLoginCallback)
                    mTCLoginCallback.onSuccess();
            }
        });
    }

    /**
     * imsdk登出
     */
    public static void imLogout() {
        TIMManager.getInstance().logout(new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
            }

            @Override
            public void onSuccess() {
            }
        });

    }


}

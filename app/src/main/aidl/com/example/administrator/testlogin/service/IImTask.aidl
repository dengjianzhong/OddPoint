// IImTask.aidl
package com.example.administrator.testlogin.service;

import com.example.administrator.testlogin.service.ITaskListener;

interface IImTask {
    void registerListener(ITaskListener listener);
    void unregisterListener(ITaskListener listener);
    void sendMsg(String msg);
}

# Enc_Hook
### Hook Java内的API加密算法
### 日志查看
```
adb logcat -s Enc_Hook
```
### 日志保存
```
adb logcat -s Enc_Hook -v thread > log.txt
```
这里会输出很多日志,日志被批量保存在data/local/tmp/*.log目录下,软件点击清空日志,同样会把tmp目录下的日志清空
也可以通过adb保存和读取

# Self_Hook
### 日志查看
```
adb logcat -s Enc_Hook
```
### 日志清除
```
adb logcat -c
```
电脑端推送Hook,自定义Hook的类,查看或修改传入和返回的参数,或静态变量
配置文件在data/local/tmp/self_hook.json

# Just
### 日志查看
```
adb logcat -s Just
```
Hook ssl 的类 使其信任用户

# Trace
### 日志查看
```
adb logcat -s Trace
```
打印方法调用栈

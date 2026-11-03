#!/bin/bash
set -ex
npm install -g appium@3.9.0
appium -v
appium driver install uiautomator2@8.7.3
appium --allow-insecure='*:adb_shell' --allow-cors --log-level debug &>/dev/null &

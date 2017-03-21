/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   startSocket.cpp
 * Author: glebmillenium
 * 
 * Created on 21 марта 2017 г., 0:52
 */

#include "startSocket.h"

startSocket::startSocket() {
    t = thread(startInteractive);
    startExchange();
}

startSocket::startSocket(const startSocket& orig) {
}

startSocket::~startSocket() {
    t.join();
}


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   startSocket.h
 * Author: glebmillenium
 *
 * Created on 21 марта 2017 г., 0:52
 */

#ifndef STARTSOCKET_H
#define STARTSOCKET_H
#include <thread>
#include "ManagerSocket.h"
using namespace std;

class startSocket {
public:

    static void startInteractive() {
        ManagerSocket *interactive;
        interactive = new ManagerSocket(5901);
        interactive->run();
    }

    static void startExchange() {
        ManagerSocket *exchange;
        exchange = new ManagerSocket(5902, true);
        exchange->run();
        
    }
    
    startSocket();
    startSocket(const startSocket& orig);
    virtual ~startSocket();
private:
    thread t;
};

#endif /* STARTSOCKET_H */


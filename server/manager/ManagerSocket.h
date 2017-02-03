/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   ManagerSocket.h
 * Author: glebmillenium
 *
 * Created on 21 января 2017 г., 11:03
 */

#ifndef MANAGERSOCKET_H
#define MANAGERSOCKET_H

/**
 * sys/types.h - различные типы данных
 * sys/socket.h - работа с сокетом
 * sys/time.h - работа со временем (С lang)
 * netinet/in.h - работа с протоколом in
 * stdio - консоль ввода/вывода
 * unistd.h - функции, константы POSIX (Ip, mac ..)
 * fcntl.h - открытие и вывод каталогов
 * algorithm - работа с каталогами
 * set - реализация контейнера
 *
 */

#include <iostream>
#include <sys/types.h>
#include <sys/socket.h>
#include <sys/time.h>
#include <netinet/in.h>
#include <stdio.h>
#include <unistd.h>
#include <fcntl.h>
#include <algorithm>
#include <set>
#include <cstring>
#include <string>
using namespace std;

/**
 * Class ManagerSocket - listener socket on network Ethernet
 * on safely chanel (SOCK_STREAM)
 *
 */
class ManagerSocket
{
private:
    /**
     * Field data class
     */
    int listener;
    struct sockaddr_in addr;
    int bytes_read;
public:
    ManagerSocket();
    ManagerSocket(int port);
    ManagerSocket(int ip, int port, int sock, int type_sock, int type_protocol = 0);
    ~ManagerSocket();
    void run();
};

#endif /* MANAGERSOCKET_H */


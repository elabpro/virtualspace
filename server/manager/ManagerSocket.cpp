/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   ManagerSocket.cpp
 * Author: glebmillenium
 * 
 * Created on 21 января 2017 г., 11:03
 */

#include "ManagerSocket.h"
#include <fstream>

ManagerSocket::ManagerSocket() {
    this->listener = socket(AF_INET, SOCK_STREAM, 0);
    this->addr.sin_family = AF_INET;
    this->addr.sin_port = htons(5901);
    this->addr.sin_addr.s_addr = INADDR_ANY;
    connectorDB = new ConnectorDB();
}

ManagerSocket::ManagerSocket(int port) {
    this->listener = socket(AF_INET, SOCK_STREAM, 0);
    this->addr.sin_family = AF_INET;
    this->addr.sin_port = htons(port);
    this->addr.sin_addr.s_addr = INADDR_ANY;
    connectorDB = new ConnectorDB();
}

ManagerSocket::ManagerSocket(int ip, int port, int sock, int type_sock, int type_protocol) {
    this->listener = socket(sock, type_sock, type_protocol);
    this->addr.sin_family = sock;
    this->addr.sin_port = htons(port);
    this->addr.sin_addr.s_addr = ip;
    connectorDB = new ConnectorDB();
}

void ManagerSocket::run() {
    /* Простаивание потоков в ожидании коннекта ~15sec*/
    timeval timeout;
    timeout.tv_sec = 15;
    timeout.tv_usec = 0;

    if (this->listener < 0) {
        perror("socket");
        exit(1);
    }

    fcntl(this->listener, F_SETFL, O_NONBLOCK);

    if (bind(this->listener, (struct sockaddr *) & this->addr, sizeof (this->addr)) < 0) {
        perror("bind");
        exit(2);
    }

    listen(listener, 10);

    set<int> clients;
    clients.clear();

    while (1) {
        // Заполняем множество сокетов
        fd_set readset;
        FD_ZERO(&readset);
        FD_SET(listener, &readset);

        for (set<int>::iterator it = clients.begin(); it != clients.end(); it++)
            FD_SET(*it, &readset);

        // Задаём таймаут
        // Ждём события в одном из сокетов
        int mx = max(listener, *max_element(clients.begin(), clients.end()));
        if (select(mx + 1, &readset, NULL, NULL, &timeout) <= 0) {
            continue;
        }

        // Определяем тип события и выполняем соответствующие действия
        if (FD_ISSET(listener, &readset)) {
            // Поступил новый запрос на соединение, используем accept
            int sock = accept(listener, NULL, NULL);
            if (sock < 0) {
                perror("accept");
                exit(3);
            }

            fcntl(sock, F_SETFL, O_NONBLOCK);
            clients.insert(sock);
        }

        for (set<int>::iterator it = clients.begin(); it != clients.end(); it++) {
            if (FD_ISSET(*it, &readset)) {
                char* data_client = new char[1024];
                int bytes_read = read(*it, data_client, 1024);
                if (bytes_read <= 0) {
                    close(*it);
                    clients.erase(*it);
                    continue;
                }
                
                if (!strcmp(data_client, (char*) "--intellectual")) {
                    intellectualManage(*it);
                    continue;
                }
                char* data_answer = connectorDB->getAnswerToClient(data_client);
                send(*it, data_answer, strlen(data_answer), 0);
            }
        }
    }
}

void ManagerSocket::intellectualManage(int sock) {
    vector<string> history = connectorDB->getHistoryAction();

    tuple<int, vector < string>> action =
            SequenceTreatmenter::run(history, 3);
    // Для начала последовательность из 3 ищем
    vector<string> sequence = std::get<1>(action);
    if (sequence.size() != 0) {
        char* data_answer = new char[1024];
        char* data_client = new char[1024];
        strcpy(data_answer, (char*) "");
        for (int i = 0; i < sequence.size(); i++) {
            strcat(data_answer, (char*) sequence.at(i).c_str());
            strcat(data_answer, (char*) "\n");
        }
        send(sock, data_answer, strlen(data_answer), 0);
    } else {
        send(sock, " ", 1, 0);
    }
}
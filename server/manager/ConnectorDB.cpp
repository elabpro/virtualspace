/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   ConnectorDB.cpp
 * Author: glebmillenium
 * 
 * Created on 18 февраля 2017 г., 1:58
 */

#include "ConnectorDB.h"

using namespace std;

ConnectorDB::ConnectorDB() {
    try {
        driver = get_driver_instance();
        con = driver->connect("tcp://127.0.0.1:3306", "vnc", "1111");
        con->setSchema("vncserver");
    } catch (sql::SQLException &e) {
        std::cout << "ERR: " << e.what();
    }
}

ConnectorDB::ConnectorDB(const ConnectorDB& orig) {
}

ConnectorDB::~ConnectorDB() {
}

char* ConnectorDB::run(char* condition) {
    cout << endl;
    char* result;
    try {
        sql::Statement *stmt;
        sql::ResultSet *res;
        char* query = new char[256];
        strcpy(query, "");
        strcat(query, "SELECT COUNT(*) FROM commands WHERE message = '");
        strcat(query, condition);
        strcat(query, "'");
        stmt = con->createStatement();
        res = stmt->executeQuery(query);
        res->next();
        if (res->getInt(1) == 0) {
            result = (char *) "неизвестная команда";
        } else {
            query = new char[256];
            strcpy(query, "");
            strcat(query, "SELECT * FROM commands WHERE message = '");
            strcat(query, condition);
            strcat(query, "'");
            stmt = con->createStatement();
            res = stmt->executeQuery(query);
            sql::SQLString command;
            while (res->next()) {
                sql::SQLString str = res->getString("answer");
                command = res->getString("id_command");
                result = SQLStringToChar(str);
                string console = res->getString("path_script");
                system(console.c_str());
            }

            sql::PreparedStatement *pstmt;



            char* insert = new char[1024];
            strcpy(insert, "");
            strcat(insert, "INSERT INTO history  VALUES(20110101101055, '");
            //YYYYMMDDHHMMSS
            strcat(insert, SQLStringToChar(command));
            strcat(insert, "', 1, 1)");
            pstmt = con->prepareStatement(insert);
            pstmt->executeUpdate();
        }

        delete res;
        delete stmt;
        return result;
    } catch (sql::SQLException &e) {
        cout << "# ERR: SQLException in " << __FILE__;
        cout << "# ERR: " << e.what();
        cout << " (MySQL error code: " << e.getErrorCode();
        cout << ", SQLState: " << e.getSQLState() << " )" << endl;
        result = (char *) "ошибка";
    }
    return result;
}

char* ConnectorDB::getCurrentTime() {
    char* buffer = new char[80];
    time_t seconds = time(NULL);
    tm* timeinfo = localtime(&seconds);
    char* format = (char*) "%Y%m%d%H%M%S";
    strftime(buffer, 80, format, timeinfo);
    return buffer;
}

char* ConnectorDB::SQLStringToChar(sql::SQLString str) {
    char* result = new char[1024];
    int i;
    for (i = 0; i < 1024; i++) {
        if (str[i] == '\0') {
            result[i] = '\0';
            break;
        }
        result[i] = str[i];
    }
    return result;
}
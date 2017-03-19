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

char* ConnectorDB::getAnswerToClient(char* condition) {
    char* result = new char[1024];
    strcpy(result, (char*) "");
    try {
        sql::Statement *stmt;
        sql::ResultSet *res;
        char* query = new char[256];
        sprintf(query,
                "SELECT * FROM commands WHERE message = '%s'",
                condition);
        stmt = con->createStatement();
        res = stmt->executeQuery(query);
        if (!res->next()) {
            strcat(result, (char *) "неизвестная команда\0");
        } else {
            sql::SQLString command;
            sql::SQLString str = res->getString("answer");
            command = res->getString("id_command");
            strcat(result, SQLStringToChar(str));

            string console = res->getString("console");
            system(console.c_str());
            sql::PreparedStatement *pstmt;
            char* insert = new char[1024];
            sprintf(insert,
                    "INSERT INTO history(date, id_command)"
                    " VALUES(%s, '%s')",
                    getCurrentTime(), SQLStringToChar(command));

            //id_command = command AND 1 - all
            pstmt = con->prepareStatement(insert);
            pstmt->executeUpdate();
            sprintf(query,
                    "SELECT * FROM commands "
                    "WHERE (id_application = %s) OR (id_application = 1)",
                    SQLStringToChar(command));
            stmt = con->createStatement();
            res = stmt->executeQuery(query);
            while (res->next()) {
                strcat(result, "\n");
                command = res->getString("message");
                strcat(result, SQLStringToChar(command));
            }
            strcat(result, "\n\0");
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

char* ConnectorDB::getDefaultCommands() {
    char* result = new char[1024];
    strcpy(result, (char*) "");
    try {
        sql::Statement *stmt;
        sql::ResultSet *res;
        sql::SQLString command;
        char* query = new char[256];
        sprintf(query,
                "SELECT * FROM commands "
                "WHERE id_application = 1");
        stmt = con->createStatement();
        res = stmt->executeQuery(query);
        while (res->next()) {
            command = res->getString("message");
            strcat(result, SQLStringToChar(command));
            strcat(result, "\n");
        }
        strcat(result, "\n\0");
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

vector<string> ConnectorDB::getHistoryAction() {
    vector<string> result;
    try {
        sql::Statement *stmt;
        sql::ResultSet *res;
        char* query = new char[256];
        sprintf(query, "SELECT * "
                "FROM history JOIN commands "
                "WHERE history.id_command = commands.id_command");
        stmt = con->createStatement();
        res = stmt->executeQuery(query);
        if (!res->next()) {
            return result;
        } else {
            sql::SQLString command;
            do {
                command = res->getString("message");
                string str(SQLStringToChar(command));
                result.push_back(str);
            } while (res->next());
        }
        delete res;
        delete stmt;
    } catch (sql::SQLException &e) {
        cout << "# ERR: SQLException in " << __FILE__;
        cout << "# ERR: " << e.what();
        cout << " (MySQL error code: " << e.getErrorCode();
        cout << ", SQLState: " << e.getSQLState() << " )" << endl;
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


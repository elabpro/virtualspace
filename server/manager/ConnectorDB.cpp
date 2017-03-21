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
        cout << "constructor" << endl;
    }
}

ConnectorDB::ConnectorDB(const ConnectorDB& orig) {
}

ConnectorDB::~ConnectorDB() {
}

char* ConnectorDB::getAnswerToClient(char* condition) {
    char* result;
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
            result = (char *) "неизвестная команда";
        } else {
            sql::SQLString command;
            sql::SQLString application;
            sql::SQLString answer = res->getString("answer");
            command = res->getString("id_command");
            application = res->getString("start_application");
            result = SQLStringToChar(answer);
            string console = res->getString("console");
            system(console.c_str());
            sql::PreparedStatement *pstmt;
            char* insert = new char[1024];
            sprintf(insert,
                    "INSERT INTO history(date, id_command)"
                    " VALUES(%s, '%s')",
                    getCurrentTime(), SQLStringToChar(command));
            pstmt = con->prepareStatement(insert);
            pstmt->executeUpdate();
            sprintf(query,
                    "SELECT * FROM commands "
                    "WHERE (id_application = %s)",
                    SQLStringToChar(application));
            stmt = con->createStatement();
            res = stmt->executeQuery(query);

            int i = setDefaultCommandsInAvailaibleTable();
            while (res->next()) {
                command = res->getString("id_command");
                sprintf(insert,
                        "INSERT INTO available_commands(id, id_command)"
                        " VALUES(%d, %s)", i, SQLStringToChar(command));
                pstmt = con->prepareStatement(insert);
                pstmt->executeUpdate();
                i++;
            }
            updateOpcode();
        }
        delete res;
        delete stmt;
    } catch (sql::SQLException &e) {
        cout << "# ERR: SQLException in " << __FILE__;
        cout << "# ERR: " << e.what();
        cout << "function: " << "getAnswerToClient" << endl;
        cout << " (MySQL error code: " << e.getErrorCode();
        cout << ", SQLState: " << e.getSQLState() << " )" << endl;
        result = (char *) "ошибка";
    }
    strcat(result, (char*) "\0");
    return result;
}

void ConnectorDB::updateOpcode() {
    try {
        char* query = new char[256];
        sprintf(query,
                "SELECT * FROM last_update_state_system");
        sql::Statement *stmt = con->createStatement();
        sql::ResultSet *res = stmt->executeQuery(query);
        res->next();

        int opcode = res->getInt("opcode");
        opcode++;


        char* insert = new char[128];
        sprintf(insert,
                "UPDATE last_update_state_system"
                " SET opcode = %d", opcode);
        sql::PreparedStatement *pstmt;
        pstmt = con->prepareStatement(insert);
        pstmt->executeUpdate();
    } catch (sql::SQLException &e) {
        cout << "# ERR: SQLException in " << __FILE__;
        cout << "function: " << "updateOpcode" << endl;
        cout << "# ERR: " << e.what();
        cout << " (MySQL error code: " << e.getErrorCode();
        cout << ", SQLState: " << e.getSQLState() << " )" << endl;
    }
}

char* ConnectorDB::getCurrentOpcode() {
    char* query = new char[256];
    sprintf(query,
            "SELECT * FROM last_update_state_system");
    sql::Statement *stmt = con->createStatement();
    sql::ResultSet *res = stmt->executeQuery(query);
    res->next();
    return SQLStringToChar(res->getString("opcode"));
}

void ConnectorDB::setOpcode(int opcode) {
    try {

        char* insert = new char[128];
        sprintf(insert,
                "UPDATE last_update_state_system"
                " SET opcode = %d", opcode);
        sql::PreparedStatement *pstmt;
        pstmt = con->prepareStatement(insert);
        pstmt->executeUpdate();
    } catch (sql::SQLException &e) {
        cout << "# ERR: SQLException in " << __FILE__;
        cout << "function: " << "setOpcode" << endl;
        cout << "# ERR: " << e.what();
        cout << " (MySQL error code: " << e.getErrorCode();
        cout << ", SQLState: " << e.getSQLState() << " )" << endl;
    }
}

char* ConnectorDB::getCommandsFromAvailaibleTable() {

    char* result = new char[16384];
    strcpy(result, "");

    try {
        char* query = new char[128];
        sql::SQLString command;
        sprintf(query, "SELECT * FROM available_commands JOIN commands "
                "WHERE available_commands.id_command = commands.id_command");
        sql::Statement *stmt = con->createStatement();
        sql::ResultSet *res = stmt->executeQuery(query);
        while (res->next()) {
            command = res->getString("message");
            strcat(result, SQLStringToChar(command));
            strcat(result, (char*) "\n");
        }
    } catch (sql::SQLException &e) {
        cout << "# ERR: SQLException in " << __FILE__;
        cout << "# ERR: function in getCommandsFromAvailaibleTable";
        cout << "# ERR: " << e.what();
        cout << " (MySQL error code: " << e.getErrorCode();
        cout << ", SQLState: " << e.getSQLState() << " )" << endl;
        result = (char*) "";
    }
    return result;
}

int ConnectorDB::setDefaultCommandsInAvailaibleTable() {
    char* query = new char[128];
    char* insert = new char[128];
    sprintf(query,
            "SELECT * FROM commands "
            "WHERE (id_application = 1)");
    sql::Statement *stmt = con->createStatement();
    sql::ResultSet *res = stmt->executeQuery(query);

    sql::SQLString command;
    sql::PreparedStatement *pstmt;
    pstmt = con->prepareStatement("TRUNCATE TABLE available_commands");
    pstmt->executeUpdate();
    int i = 0;
    while (res->next()) {
        command = res->getString("id_command");
        sprintf(insert,
                "INSERT INTO available_commands(id, id_command)"
                " VALUES(%d, %s)", i, SQLStringToChar(command));
        pstmt = con->prepareStatement(insert);
        pstmt->executeUpdate();
        i++;
    }
    return i;
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
        cout << "function: getHistoryAction " << endl;
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

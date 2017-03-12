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

#include "mysql_connection.h"

#include "cppconn/driver.h"
#include "cppconn/exception.h"
#include "cppconn/resultset.h"
#include "cppconn/statement.h"

using namespace std;

ConnectorDB::ConnectorDB() {
    //mysql_init(&mysql);
    //connection = mysql_real_connect(&mysql,"host","user",
    //                   "password","database",port,"unix_socket",clientflag);
    //connection = mysql_real_connect(&mysql,"localhost",
    //"vnc","1111","vncserver", 3306, 0, 0);
    try {
        sql::Driver *driver;
        sql::Connection *con;
        sql::Statement *stmt;
        sql::ResultSet *res;

        driver = get_driver_instance();

    } catch (sql::SQLException &e) {
        std::cout << "ERR: " << e.what();
    }
}

ConnectorDB::ConnectorDB(const ConnectorDB& orig) {
}

ConnectorDB::~ConnectorDB() {
}

const char* ConnectorDB::run(char* condition) {
    cout << endl;
    const char* result;
    try {
        sql::Driver *driver;
        sql::Connection *con;
        
        sql::Statement *stmt;
        sql::ResultSet *res;

        /* Create a connection */
        driver = get_driver_instance();
        con = driver->connect("tcp://127.0.0.1:3306", "vnc", "1111");
        /* Connect to the MySQL test database */
        con->setSchema("vncserver");
        
        char* query = new char[256];
        strcpy(query, "");
        strcat(query, "SELECT COUNT(*) FROM commands WHERE message = '");
        strcat(query, condition);
        strcat(query, "'");
        stmt = con->createStatement();
        res = stmt->executeQuery(query);
        res->next();
        if (res->getInt(1) == 0) {
            result = "неизвестная команда";
        } else {
            query = new char[256];
            strcpy(query, "");
            strcat(query, "SELECT * FROM commands WHERE message = '");
            strcat(query, condition);
            strcat(query, "'");
            stmt = con->createStatement();
            res = stmt->executeQuery(query);
            while (res->next()) {
                sql::SQLString str = res->getString("answer");
                sql::SQLString command = res->getString("id_command");
                query = new char[256];
                /*strcpy(query, "");
                strcat(query, "INSERT INTO history  (id_command) VALUES('");
                strcat(query, command);
                strcat(query, "')");
                stmt->executeQuery(query);*/
                
                char* str2 = new char[str.length()];
                for(int i = 0; i < str.length(); i++){
                    str2[i] = str[i];
                }
                result = str2;
                string console = res->getString("path_script");
                system(console.c_str());
            }
        }
        delete res;
        delete stmt;
        delete con;
        return result;
    } catch (sql::SQLException &e) {
        cout << "# ERR: SQLException in " << __FILE__;
        cout << "# ERR: " << e.what();
        cout << " (MySQL error code: " << e.getErrorCode();
        cout << ", SQLState: " << e.getSQLState() << " )" << endl;

    }
    result = "неизвестная команда";
    return result;
}


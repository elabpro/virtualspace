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

string ConnectorDB::run(char* condition) {
    cout << endl;
    string result;
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
        strcat(query, "SELECT * FROM commands WHERE message = '");
        strcat(query, condition);
        strcat(query, "'");
        stmt = con->createStatement();
        res = stmt->executeQuery(query);
        while (res->next()) {
            result = res->getString("answer");
            string console = res->getString("path_script");
            system(console.c_str());
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


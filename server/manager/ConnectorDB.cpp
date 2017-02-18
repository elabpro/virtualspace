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

int ConnectorDB::run() {
    cout << endl;

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

        stmt = con->createStatement();
        res = stmt->executeQuery("SELECT * FROM commands");
        while (res->next()) {
            cout << "\t... MySQL replies: ";
            /* Access column data by alias or column name */
            cout << res->getString("message") << endl;
            cout << res->getString("answer") << endl;
            //cout << "\t... MySQL says it again: ";
            /* Access column fata by numeric offset, 1 is the first column */
            //cout << res->getString(1) << endl;
        }
        cout << "END";
        delete res;
        delete stmt;
        delete con;

    } catch (sql::SQLException &e) {
        cout << "# ERR: SQLException in " << __FILE__;
        cout << "# ERR: " << e.what();
        cout << " (MySQL error code: " << e.getErrorCode();
        cout << ", SQLState: " << e.getSQLState() << " )" << endl;

    }

    cout << endl;

    return EXIT_SUCCESS;
}


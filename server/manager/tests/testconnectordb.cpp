/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * File:   testconnectordb.cpp
 * Author: glebmillenium
 *
 * Created on 13.03.2017, 11:15:19
 */

#include "testconnectordb.h"
#include "../ConnectorDB.h"


CPPUNIT_TEST_SUITE_REGISTRATION(testconnectordb);

testconnectordb::testconnectordb() {
}

testconnectordb::~testconnectordb() {
}

void testconnectordb::setUp() {
}

void testconnectordb::tearDown() {
}

void testconnectordb::testRun() {
    char* condition = "ева закрой почту";
    char* conder = "почта закрыта";
    const char* result = ConnectorDB::run(condition);
    if (strcmp(result, conder) == 0) {
        CPPUNIT_ASSERT(true);
    } else {
        CPPUNIT_ASSERT(false);
    }
}


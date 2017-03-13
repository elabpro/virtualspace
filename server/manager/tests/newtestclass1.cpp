/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * File:   newtestclass1.cpp
 * Author: glebmillenium
 *
 * Created on 13.03.2017, 11:27:04
 */

#include "newtestclass1.h"
#include "../ConnectorDB.h"


CPPUNIT_TEST_SUITE_REGISTRATION(newtestclass1);

newtestclass1::newtestclass1() {
}

newtestclass1::~newtestclass1() {
}

void newtestclass1::setUp() {
}

void newtestclass1::tearDown() {
}

void newtestclass1::testMe() {
    char* condition = "ева закрой почту";
    char* conder = "почта закрыта";
    const char* result = ConnectorDB::run(condition);
    if (strcmp(result, conder) == 0) {
        CPPUNIT_ASSERT(true);
    } else {
        CPPUNIT_ASSERT(false);
    }
}


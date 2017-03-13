/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * File:   testconnectordb.h
 * Author: glebmillenium
 *
 * Created on 13.03.2017, 11:15:17
 */

#ifndef TESTCONNECTORDB_H
#define TESTCONNECTORDB_H

#include <cppunit/extensions/HelperMacros.h>

class testconnectordb : public CPPUNIT_NS::TestFixture {
    CPPUNIT_TEST_SUITE(testconnectordb);

    CPPUNIT_TEST(testRun);

    CPPUNIT_TEST_SUITE_END();

public:
    testconnectordb();
    virtual ~testconnectordb();
    void setUp();
    void tearDown();

private:
    void testRun();

};

#endif /* TESTCONNECTORDB_H */


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * File:   newtestclass.cpp
 * Author: glebmillenium
 *
 * Created on 24.01.2017, 13:41:56
 */

#include "newtestclass.h"
#include "../SequenceTreatmenter.h"


CPPUNIT_TEST_SUITE_REGISTRATION(newtestclass);

newtestclass::newtestclass() {
}

newtestclass::~newtestclass() {
}

void newtestclass::setUp() {
}

void newtestclass::tearDown() {
}

void newtestclass::testRun1() {
    vector<string> input {"CallFirefox", "CallApp", "CallFirefox", 
            "CallApp", "CallFirefox", "CallNote", "CallApp", "CallFirefox"};
    vector<string> equ {"CallApp", "CallFirefox"};
    tuple<int, vector<string>> result = SequenceTreatmenter::run(input, 2);
    if ((std::get<0>(result) == 3) && (std::get<1>(result) == equ)) {
        CPPUNIT_ASSERT(true);
    } else {
        CPPUNIT_ASSERT(false);
    }
}

void newtestclass::testRun2() {
    vector<string> input {"CallFirefox", "CallApp", "CallOffice", 
            "CallNote", "CallNetbeans", "CallFirefox", "CallApp", "CallOffice",
            "CallQT", "CallFirefox", "CallApp", "CallOffice", "CallNotepad",
            "CallFirefox", "CallApp", "CallOffice"};
    vector<string> equ {"CallFirefox", "CallApp", "CallOffice"};
    tuple<int, vector<string>> result = SequenceTreatmenter::run(input, 3);
    if ((std::get<0>(result) == 4) && (std::get<1>(result) == equ)) {
        CPPUNIT_ASSERT(true);
    } else {
        CPPUNIT_ASSERT(false);
    }
}
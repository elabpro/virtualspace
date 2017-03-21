/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   main.cpp
 * Author: glebmillenium
 *
 * Created on 21 января 2017 г., 11:03
 */

#include <cstdlib>
#include "ManagerSocket.h"
#include "SequenceTreatmenter.h"
#include "startSocket.h"
#include <iostream>
#include "ConnectorDB.h"
#include <thread>
using namespace std;



/*
 * 
 */
int main(int argc, char** argv) {
    (new startSocket);
    
    return 0;
}

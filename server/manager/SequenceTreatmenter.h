/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   SequenceTreatmenter.h
 * Author: glebmillenium
 *
 * Created on 22 января 2017 г., 12:56
 */

#ifndef SEQUENCETREATMENTER_H
#define SEQUENCETREATMENTER_H

#include <vector>
#include <string>
#include <tuple>
using namespace std;

class SequenceTreatmenter {
public:
    static tuple<int, vector<string>> run(vector<string> sequence, int i);
private:
    static int countRepeat(vector<string> seq, vector<string> subseq);
};

#endif /* SEQUENCETREATMENTER_H */

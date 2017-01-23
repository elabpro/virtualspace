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
    SequenceTreatmenter();
    SequenceTreatmenter(const SequenceTreatmenter& orig);
    SequenceTreatmenter(vector<string> sequence);
    virtual ~SequenceTreatmenter();
    void run();
private:
    vector<string> sequence;
    int countRepeat(vector<string> seq, vector<string> subseq);
};

#endif /* SEQUENCETREATMENTER_H */


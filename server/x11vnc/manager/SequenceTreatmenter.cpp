/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   SequenceTreatmenter.cpp
 * Author: glebmillenium
 * 
 * Created on 22 января 2017 г., 12:56
 */

#include "SequenceTreatmenter.h"

SequenceTreatmenter::SequenceTreatmenter() 
{
}

SequenceTreatmenter::SequenceTreatmenter(vector<string> sequence) 
{
    this->sequence = sequence;
}

void SequenceTreatmenter::run()
{
    vector<tuple<int, vector<string>, int>> result;
    for(int i = 1; i <= this->sequence.size(); i++)
    {
        for(int j = 0; j < this->sequence.size() - i + 1; j++)
        {
            vector<string> temp;
            for(int k = j; k <= j + i - 1; k++)
            {
                temp.push_back(sequence[j]);
            }
            int ka = countRepeat(sequence, temp);
            
           //tuple<int, vector<string>, int> iter(i, temp, countRepeat(sequence, temp));
            //result.push_back(iter);
        }
    }
    result;
}

SequenceTreatmenter::SequenceTreatmenter(const SequenceTreatmenter& orig) 
{
}

int SequenceTreatmenter::countRepeat(vector<string> seq, vector<string> subseq)
{
    int count = 0;
    for(int i = 0; i < seq.size() - subseq.size() + 1; i++)
    {
        int j = 0;
        do{
            if(seq[i] == subseq[j])
                j++;
            else
                break;
        }while(j < subseq.size());
        if(j == subseq.size()) count++;      
    }
    return count;
}

SequenceTreatmenter::~SequenceTreatmenter() 
{
}


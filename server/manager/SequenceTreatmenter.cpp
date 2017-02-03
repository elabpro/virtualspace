/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   SequenceTreatmenter.cpp
 * Author: sketch
 * 
 * Created on 22 января 2017 г., 12:56
 */

#include "SequenceTreatmenter.h"

/**
 * run - статический метод для получения часто 
 * повторяющейся под последовательности в заданной 
 * последовательности команд сервера
 * 
 * @param sequence - массив последовательных команд
 * @param window - длина последовательности 
 *                  (по умолчанию выбирается поиск 
 *                  подпоследовательности длиной в 2 команды)
 * @return tuple<int, vector<string>> кортеж следующего вида: 
 *          количество повторений в заданной последовательности, 
 *          массив найденной подпоследовательности команд
 */
tuple<int, vector<string>> SequenceTreatmenter::run(vector<string> sequence, int window = 2)
{
    vector<tuple<int, vector<string>>> result;

    for(int j = 0; j < sequence.size() - window + 1; j++)
    {
        vector<string> temporarySubSequence;
        for(int k = j; k <= j + window - 1; k++)
        {
            temporarySubSequence.push_back(sequence[k]);
        }
        result.push_back(
            std::make_tuple(countRepeat(sequence, temporarySubSequence), 
                temporarySubSequence));
    }
    
    int maxCountRepeat = std::get<0>(result[0]);
    vector<string> sequenceMaxCountRepeat = std::get<1>(result[0]);
    
    for(int j = 0; j < result.size(); j++)
    {
        if(std::get<0>(result[j]) > maxCountRepeat) {
            maxCountRepeat = std::get<0>(result[j]);
            sequenceMaxCountRepeat = std::get<1>(result[j]);
        }
    }
    return std::make_tuple(maxCountRepeat, sequenceMaxCountRepeat);
}

/**
 * countRepeat - статический метод подсчитывает количество 
 * повторения подпоследовательности subseq в заданной последовательности seq
 * 
 * @param seq - последовательность
 * @param subseq - искомая подпоследовательность
 * @return int - количество повторений subseq в seq
 */
int SequenceTreatmenter::countRepeat(vector<string> seq, vector<string> subseq)
{
    int count = 0;
    for(int i = 0; i < seq.size() - subseq.size() + 1; i++)
    {
        int j = 0;
        do{
            if(seq[i + j] == subseq[j])
                j++;
            else
                break;
        }while(j < subseq.size());
        if(j == subseq.size()) count++;      
    }
    return count;
}

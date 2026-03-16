package com.resaka.service;

import com.resaka.model.Note;
import com.resaka.model.Parametre;
import java.util.List;

public class ResultatService {

    public static double calculateFinalGrade(List<Note> notes, List<Parametre> parametres) {
        if (notes == null || notes.isEmpty()) {
            return 0.0;
        }
        if (notes.size() == 1) {
            return notes.get(0).getValeurNote();
        }

        double totalGap = 0;
        for (int i = 0; i < notes.size(); i++) {
            for (int j = i + 1; j < notes.size(); j++) {
                totalGap += Math.abs(notes.get(i).getValeurNote() - notes.get(j).getValeurNote());
            }
        }

        Parametre matchedParametre = null;
        for (Parametre p : parametres) {
            if (p.getOperateur() == null || p.getOperateur().getSymbole() == null) continue;
            
            String symb = p.getOperateur().getSymbole();
            boolean isMatch = false;

            switch (symb) {
                case "<": isMatch = (totalGap < p.getMax()); break;
                case ">=": isMatch = (totalGap >= p.getMin()); break;
                case "<=": isMatch = (totalGap <= p.getMax()); break;
                case ">": isMatch = (totalGap > p.getMin()); break;
                default: isMatch = (totalGap >= p.getMin() && totalGap <= p.getMax()); break;
            }

            if (isMatch) {
                matchedParametre = p;
                break;
            }
        }

        if (matchedParametre == null && parametres != null && !parametres.isEmpty()) {
            Parametre closestParam = null;
            double minDifference = Double.MAX_VALUE;

            for (Parametre p : parametres) {
                double maxVal = p.getMax();
                double minVal = p.getMin();
                
                if (maxVal <= totalGap) {
                    double diff = totalGap - maxVal;
                    if (diff < minDifference) {
                        minDifference = diff;
                        closestParam = p;
                    }
                }
                
                if (minVal <= totalGap) {
                    double diff = totalGap - minVal;
                    if (diff < minDifference) {
                        minDifference = diff;
                        closestParam = p;
                    }
                }
            }

            if (closestParam == null) {
                double bestMinVal = Double.MAX_VALUE;
                for (Parametre p : parametres) {
                    double currentMinParamVal = Math.min(p.getMin(), p.getMax());
                    if (closestParam == null || currentMinParamVal < bestMinVal) {
                        closestParam = p;
                        bestMinVal = currentMinParamVal;
                    }
                }
            }
            matchedParametre = closestParam;
        }

        if (matchedParametre == null) {
            return 0.0; // No rule applies
        }

        double finalGrade = 0;
        int resId = matchedParametre.getIdResolution();

        if (resId == 1) {
            finalGrade = notes.stream().mapToDouble(Note::getValeurNote).average().orElse(0);
        } else if (resId == 2) {
            finalGrade = notes.stream().mapToDouble(Note::getValeurNote).max().orElse(0);
        } else if (resId == 3) {
            finalGrade = notes.stream().mapToDouble(Note::getValeurNote).min().orElse(0);
        } else {
            // Fallback to symbol logic if id_resolution mapping isn't fully set
            String opSymb = matchedParametre.getOperateur().getSymbole();
            if ("+".equals(opSymb)) {
                 finalGrade = notes.stream().mapToDouble(Note::getValeurNote).max().orElse(0);
            } else if ("-".equals(opSymb)) {
                 finalGrade = notes.stream().mapToDouble(Note::getValeurNote).min().orElse(0);
            } else {
                 finalGrade = notes.stream().mapToDouble(Note::getValeurNote).average().orElse(0);
            }
        }

        return Math.round(finalGrade * 100.0) / 100.0;
    }
}

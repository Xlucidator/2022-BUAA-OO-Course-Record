package func;

import parse.Lexer;

import java.util.ArrayList;

public class Func {
    private String name;
    private ArrayList<String> para;
    private String relation;

    public Func(Lexer lexer) {
        this.name = lexer.peek();
        this.para = new ArrayList<>();
        lexer.next();
        while (!lexer.peek().equals(")")) {
            lexer.next();
            if (lexer.peek().equals("x")) {
                para.add("t");
            } else if (lexer.peek().equals("y") | lexer.peek().equals("z")) {
                para.add(lexer.peek());
            }
        }
        lexer.next();
        lexer.next();
        StringBuilder sb = new StringBuilder();
        while (!lexer.peek().equals("")) {
            if (lexer.peek().equals("x")) {
                sb.append("t"); //利用t来替换掉全部的x，防止替换字符串的时候出现问题
            } else {
                sb.append(lexer.peek());
            }
            lexer.next();
        }
        this.relation = sb.toString();
    }

    public String getName() {
        return name;
    }

    public String getRelation() {
        return relation;
    }

    public ArrayList<String> getPara() {
        return para;
    }
}

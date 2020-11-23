package disco.bot;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class ACLParser {

//    public static void main(String[] args) throws IOException {

//        System.out.print(getACLGT4DriverStandings() + "\n");
//        System.out.print(getACLGT4TeamStandings() + "\n");
//        System.out.print(getACLGT4Preq(1) + "\n");
//        System.out.print(getACLWEKPreq(1) + "\n");
//    }


    public static String getACLGT4Preq(int roundID) throws IOException {
        String preqUrl = "https://acleague.com.pl/sezonacc1-runda" + roundID + "-wyniki-prequal.html";
        Document preqDoc = org.jsoup.Jsoup.connect(preqUrl).ignoreContentType(true).execute().bufferUp().parse();
        //System.out.println(preqDoc);
        String msg = "Stan prekwalifikacji: " + preqDoc.select("h3").text() + "\n";
        Elements preqTable = preqDoc.select("table");
        Elements rows = preqTable.select("tr");
        int j;
        for (j=1;j<6;j++) {
            Element i = rows.get(j);
            msg += printText(i);
        }
        msg += "---\n";
            for (Element i : rows) {
                for (j = 0; j < ACLNicknames.values().length; j++) {
                    if (i.text().matches(".*" + ACLNicknames.values()[j] + " .*") && Integer.parseInt(i.select("td").first().text()) > 5) {
                        msg += printText(i);
                    }
                }
            }
            return msg;
    }


    public static String getACLWEKPreq(int roundID) throws IOException {
        String preqUrl = "https://acleague.com.pl/sezonac17-runda" + roundID + "-wyniki.html";
        Document preqDoc = org.jsoup.Jsoup.connect(preqUrl).ignoreContentType(true).execute().bufferUp().parse();
        //System.out.println(preqDoc);
        String msg = "Stan prekwalifikacji: " + preqDoc.select("h3").text() + "\n\n";
        Elements preqTables = preqDoc.select("table");
        //System.out.println(preqTable);
        Elements DPIRows = preqTables.get(0).select("tr");
        Elements GT3Rows = preqTables.get(1).select("tr");
        //System.out.println(GT3Rows);
        msg += preqDoc.select("h5").get(0).text() + "\n";
        int j;
        for (j = 1; j < 4; j++) {
            Element i = DPIRows.get(j);
            msg += printText(i);
        }
        msg +="\n\n";
        msg += preqDoc.select("h5").get(1).text() + "\n";
        for (j = 1; j < 6; j++) {
            Element i = GT3Rows.get(j);
            msg += printText(i);
        }
        msg += "---\n";
        for (Element i : GT3Rows) {
            for (j = 0; j < ACLNicknames.values().length; j++) {
                if (i.text().matches(".*" + ACLNicknames.values()[j] + " .*") && Integer.parseInt(i.select("td").first().text()) > 5) {
                    msg += printText(i);
                }
            }
        }
        return msg;
    }


    public static String getACLGT4DriverStandings() throws IOException {
        String standingsUrl = "https://acleague.com.pl/sezonacc1-generalka-driver.html";
        Document standingsDoc = org.jsoup.Jsoup.connect(standingsUrl).ignoreContentType(true).execute().bufferUp().parse();
        //System.out.println(standingsDoc);

        String msg = "Klasyfikacja indywidualna ACL GT4:\n";
        Elements table = standingsDoc.select("table");
        Elements rows = table.select("tr");
        //System.out.println(rows);
        int j;
        for (j=1;j<11;j++) {
            Element i = rows.get(j);
            msg += printStandings(i);
        }
        msg += "---\n";
        for (Element i : rows) {
            for (j = 0; j < ACLNicknames.values().length; j++) {
                if (i.text().matches(".*" + ACLNicknames.values()[j] + " .*") && Integer.parseInt(i.select("td").first().text()) > 10) {
                    msg += printStandings(i);
                }
            }
        }
        return msg;
    }


    public static String getACLGT4TeamStandings() throws IOException {
        String standingsUrl = "https://acleague.com.pl/sezonacc1-generalka-team.html";
        Document standingsDoc = org.jsoup.Jsoup.connect(standingsUrl).ignoreContentType(true).execute().bufferUp().parse();
        //System.out.println(standingsDoc);

        String msg = "Klasyfikacja zespołowa ACL GT4:\n";
        Elements table = standingsDoc.select("table");
        Elements rows = table.select("tr");
        //System.out.println(rows);
        int j;
        for (j=1;j<6;j++) {
            Element i = rows.get(j);
            msg += printTeamStandings(i);
        }
        msg += "---\n";
        for (Element i : rows) {
            for (j = 0; j < ACLTeamNames.values().length; j++) {
                //System.out.println(i.getElementsByClass("dName").text());
                //System.out.println(ACLTeamNames.values()[j]);
                if (i.getElementsByClass("dName").text().matches("" + ACLTeamNames.values()[j] ) && Integer.parseInt(i.select("td").first().text()) > 5) {
                    msg += printTeamStandings(i);
                }
            }
        }
        return msg;
    }

    private static String printText(Element i) {
        String msg = i.select("td").first().text() + ". ";
        msg += i.getElementsByClass("dName").text() + " ";
        //System.out.println(i.getElementsByClass("tName").text().replace("| ", "") + " );
        msg += i.select("img[title]").first().attr("title") + " ";
        msg += i.getElementsByClass("laps").text() + " ";
        msg += i.getElementsByClass("time").text() + " ";
        if (i.select("td").first().text().matches("1")) msg += i.getElementsByClass("gap").text() + "\n";
        else msg += "+" + i.getElementsByClass("gap").text().substring(3) + "\n";
        return msg;
    }

    private static String printStandings(Element i) {
        String msg = i.select("td").first().text() + ". ";
        msg += i.getElementsByClass("dName").text() + " ";
        //System.out.println(i.getElementsByClass("tName").text().replace("| ", "") + " );
        msg += i.select("img[title]").first().attr("title") + " ";
        msg += i.select("td").last().text() + "\n";
        return msg;
    }

    private static String printTeamStandings(Element i) {
        String msg = i.select("td").first().text() + ". ";
        msg += i.getElementsByClass("dName").text() + " ";
        msg += "(" + i.getElementsByClass("teamDrivers").text() + ")" + " ";
        msg += i.select("img[title]").first().attr("title") + " - ";
        msg += i.select("td").last().text() + "\n";
        return msg;
    }

}

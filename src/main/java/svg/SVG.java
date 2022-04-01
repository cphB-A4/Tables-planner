package svg;

public class SVG {
    //Alle mål er forskudt med 100px, da vi ikke laver SVG tegning inde i svg tegning.

    final String headerTemplate = "<svg " +
            "height=\"%d%%\" " +
            "width=\"%d%%\" " +
            "viewBox=\"%s\" " +
            "x=\"%d\"   " +
            "y=\"%d\"   " +
            " preserveAspectRatio=\"xMinYMin\">";
    private final String rectTemplate = "<rect x=\"%d\" y=\"%d\" height=\"%f\" width=\"%f\" style=\"stroke:#000000; fill: #ffffff\" />";

    public void addArrowAndText(int x1, int y1, int x2, int y2, int textX, int textY, int textToDisplay, StringBuilder svg) {
        svg.append("<defs>\n" +
                "    <marker id=\"startarrow\" markerWidth=\"10\" markerHeight=\"7\" \n" +
                "    refX=\"10\" refY=\"3.5\" orient=\"auto\">\n" +
                "      <polygon points=\"10 0, 10 7, 0 3.5\" fill=\"black\" />\n" +
                "    </marker>\n" +
                "    <marker id=\"endarrow\" markerWidth=\"10\" markerHeight=\"7\" \n" +
                "    refX=\"0\" refY=\"3.5\" orient=\"auto\" markerUnits=\"strokeWidth\">\n" +
                "        <polygon points=\"0 0, 10 3.5, 0 7\" fill=\"black\" />\n" +
                "    </marker>\n" +
                "  </defs>\n" +
                "  <line x1=\"" + x1 + "\" y1=\"" + y1 + "\" x2=\"" + x2 + "\" y2=\"" + y2 + "\" stroke=\"#000\"" +
                " stroke-width=\"1.5\" \n" +
                "marker-end=\"url(#endarrow)\" marker-start=\"url(#startarrow)\" />" +
                "  <text x=\"" + textX + "\" y=\"" + textY + "\" fill=\"black\">" + textToDisplay + "cm</text>\n ");

    }

    public void addStripedLine(int x1, int x2, int y1, int y2, StringBuilder svg){
        svg.append("<line stroke-dasharray=\"6\" x1=\" " + x1
                + "\" x2=\"" + x2 + "\" y1=\"" + y1 + "\" y2=\"" + y2
                + "\" stroke-width=\"1\" stroke=\"black\"></line>\n");
    }

    public void addLine(int x1, int y1, int x2, int y2, StringBuilder svg) {
        svg.append("<line x1=\"" + x1 + "\" y1=\"" + y1 + "\" x2=\"" + x2 + "\" y2=\"" + y2 + "\" style=\"stroke:#000000; stroke-width:2\" />");
    }

    public void addRect(int x, int y, double height, double width, StringBuilder svg) {
        svg.append(String.format(rectTemplate, x, y, height, width));
    }

}

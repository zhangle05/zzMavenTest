/*
 *  Copyright 2007-2008, Plutext Pty Ltd.
 *   
 *  This file is part of docx4j.

    docx4j is licensed under the Apache License, Version 2.0 (the "License"); 
    you may not use this file except in compliance with the License. 

    You may obtain a copy of the License at 

        http://www.apache.org/licenses/LICENSE-2.0 

    Unless required by applicable law or agreed to in writing, software 
    distributed under the License is distributed on an "AS IS" BASIS, 
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
    See the License for the specific language governing permissions and 
    limitations under the License.

 */

package zz.maven.test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.Map;

import org.docx4j.openpackaging.packages.PresentationMLPackage;
import org.docx4j.openpackaging.parts.Part;
import org.docx4j.openpackaging.parts.PartName;
import org.docx4j.openpackaging.parts.PresentationML.SlidePart;
import org.pptx4j.convert.out.svginhtml.SvgExporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jharrop
 *
 */
public class RenderAsSvgInHtml {

    protected static Logger log = LoggerFactory.getLogger(RenderAsSvgInHtml.class);

    public static void main(String[] args) throws Exception {

        String inputfilepath = "/Users/zhangle/Documents/work/xtech/tmp/test.pptx";
        // String inputfilepath = System.getProperty("user.dir") +
        // "/sample-docs/pptx/lines.pptx";

        // Where to save images
        SvgExporter.setImageDirPath("/Users/zhangle/Documents/work/xtech/tmp/");

        PresentationMLPackage presentationMLPackage = (PresentationMLPackage) PresentationMLPackage
                .load(new java.io.File(inputfilepath));

        // TODO - render slides in document order!
        FileWriter fw = new FileWriter("/Users/zhangle/Documents/work/xtech/tmp/test.html");
        BufferedWriter bw = new BufferedWriter(fw);
        try {
            Iterator<Map.Entry<PartName, Part>> partIterator = presentationMLPackage.getParts().getParts().entrySet()
                    .iterator();
            while (partIterator.hasNext()) {

                Map.Entry<PartName, Part> pairs = partIterator.next();

                Part p = (Part) pairs.getValue();
                if (p instanceof SlidePart) {
                    String text = SvgExporter.svg(presentationMLPackage, (SlidePart) p);
                    bw.write(text);
                    bw.write("\n");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            bw.close();
            fw.close();
        }

        // NB: file suffix must end with .xhtml in order to see the SVG in a
        // browser
    }

}

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
import javafx.scene.layout.Pane;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MDLFileExtractor {
    // ArrayList to store the extracted block strings and blocks
    private ArrayList<String> blockStrings;
    private ArrayList<Block> blocks;

    // Constants for XML tags
    private static final String BLOCK_START_TAG = "<Block";
    private static final String BLOCK_END_TAG = "</Block>";
    private static final String SYSTEM_END_TAG = "</System>";
    private static final String POSITION_TAG = "Position";
    private static final String PORTS_TAG = "    <P Name=\"Ports\">";
    private static final String BLOCK_TYPE_TAG = "BlockType=";
    private static final String NAME_TAG = "Name";
    private static final String BLACK_MIRROR_TAG="<P Name=\"BlockMirror\">";

    public MDLFileExtractor() {
        blockStrings = new ArrayList<>();
        blocks = new ArrayList<>();
    }

    // Extracts the content within the <System> tag from the MDL file
    public static String MDLblocks(File file) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(file);
             Scanner scanner = new Scanner(fileInputStream)) {
            String data = scanner.useDelimiter("\\A").next();
            Pattern pattern = Pattern.compile("<System>(.*?)</System>", Pattern.DOTALL);
            Matcher matcher = pattern.matcher(data);
            if (matcher.find()) {
                return "<System>\n" + matcher.group(1) + "\n</System>";
            } else {
                return null;
            }
        }
    }

    // Extracts block strings from the system content
    private void blockExtractor(String system) {
        try (Scanner cursor = new Scanner(system)) {
            String line = cursor.nextLine();
            while (!line.contains(SYSTEM_END_TAG)) {
                if (line.contains(BLOCK_START_TAG)) {
                    StringBuilder blockString = new StringBuilder();
                    while (!line.contains(BLOCK_END_TAG)) {
                        blockString.append(line).append("\n");
                        line = cursor.nextLine();
                    }
                    blockString.append(BLOCK_END_TAG);
                    blockStrings.add(blockString.toString());
                }
                line = cursor.nextLine();
            }
        }
    }

    public ArrayList<String> getBlockStrings() {
        return blockStrings;
    }

    public ArrayList<Block> getBlocks() {
        return blocks;
    }

    // Extracts information from a single block string and creates a Block object
    private void singleBlockExtractor(String blockString) {
        try (Scanner scanner = new Scanner(blockString)) {
            Block block = new Block();
            String line = scanner.nextLine();
            while (!line.contains(BLOCK_END_TAG)) {
                if (line.contains(BLOCK_START_TAG)) {
                    // Extracts the block type and ID from the start tag
                    String type = line.substring(line.indexOf(BLOCK_TYPE_TAG) + 11, line.indexOf(NAME_TAG) - 2);
                    String id = line.substring(line.indexOf("ID=") + 4, line.indexOf(">") - 1);
                    block.setId(Integer.parseInt(id));
                    block.setType(type);
                }
                if (line.contains(POSITION_TAG)) {
                    // Extracts the position coordinates from the Position tag
                    String position = line.substring(line.indexOf(">[") + 2, line.indexOf("]<"));
                    String[] coordinates = position.split(", ");
                    block.setLeft(Integer.parseInt(coordinates[0]));
                    block.setTop(Integer.parseInt(coordinates[1]));
                    block.setRight(Integer.parseInt(coordinates[2]));
                    block.setBottom(Integer.parseInt(coordinates[3]));
                }
                if (line.contains(PORTS_TAG)) {
                    // Extracts the number of ports from the Ports tag
                    int startIndex = line.indexOf('[') + 1;
                    int endIndex = line.indexOf(',', startIndex);
                    if (endIndex == -1) {
                        endIndex = line.indexOf(']', startIndex);
                    }
                    String firstNumber = line.substring(startIndex, endIndex);
                    block.setPorts(Integer.parseInt(firstNumber));
                }
                if(line.contains(BLACK_MIRROR_TAG)){
                    // Sets the rotated flag if BlockMirror tag is present
                    block.setRotated(true);
                }
                line = scanner.nextLine();
            }
            blocks.add(block);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    // Extracts blocks from the system content and creates Block objects
    public void extractBlocks(String system) {
        blockExtractor(system);
        for (String blockString : blockStrings) {
            singleBlockExtractor(blockString);
            blocks.get(blocks.size() - 1).setBlock();
        }
    }

    // Draws the blocks on a JavaFX Pane
    public void draw(Pane pane) {
        for (Block block : blocks) {
            pane.getChildren().addAll(block.getFrame(), block.getShadow(), block.getText(), block.getImageView());
        }
    }
}

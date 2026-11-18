/**
 * The CONTENT of The Last Lantern: all scenes (vertices) and choices (edges).
 *
 * The story is kept here, away from StoryGraph, so the graph code does not
 * care what the story is about. To change the story, only this file changes.
 */
public class Story {
    public static final String START = "GATE";   // the starting vertex

    public static StoryGraph build() {
        StoryGraph g = new StoryGraph();

        // ---------------- VERTICES (scenes) ----------------

        g.addScene(new NormalScene("GATE", "The Gate of Still Water",
            "Midnight. The road ends at a wooden gate, and beyond it a village "
            + "where every house has a red lantern burning by its door. No dogs "
            + "bark. No one answers when you call. Every house throws a long "
            + "shadow across the street except one. Somewhere past the houses, "
            + "a girl is crying. Up the hill, a shrine door knocks against its "
            + "frame, though there is no wind. Behind you, the road you walked "
            + "in on is gone."));

        g.addScene(new NormalScene("SHADOWLESS_HOUSE", "The House Without a Shadow",
            "The door is unlocked. Inside, a table is set for a family dinner: "
            + "eight bowls of rice, still steaming. Seven pairs of chopsticks "
            + "rest neatly on their bowls. The eighth pair stands straight up "
            + "in the rice, the way incense stands for the dead. The stool in "
            + "front of it has been pulled out, as if someone was expected."));

        // This scene gives the player an item (uses the 4-argument constructor).
        g.addScene(new NormalScene("ANCESTOR_ALTAR", "The Ancestral Altar",
            "Against the back wall stands a family altar with eight wooden "
            + "spirit tablets. Seven are carved with names. The eighth is blank, "
            + "and a brush beside it is wet with fresh ink. A strip of paper is "
            + "pinned under the incense bowl: 'Each house keeps a lantern so its "
            + "names are remembered. When the last lantern goes dark, the "
            + "village forgets.' You slip the blank tablet into your coat.",
            "Blank Spirit Tablet"));

        g.addScene(new NormalScene("WELL", "The Well",
            "The crying leads you to a stone well at the edge of the village. "
            + "The sound is coming from inside it. A small voice drifts up: "
            + "'My lantern fell. It is so dark down here. Please, lower yours.'"));

        g.addScene(new NormalScene("WELL_NAME", "A Name in the Dark",
            "'Who are you?' you ask. The crying stops. After a long silence the "
            + "voice answers, and the name it gives is yours. Then, softly: "
            + "'You promised you would come back for me.' You have never been "
            + "here before. You are almost sure of it."));

        g.addScene(new NormalScene("SHRINE", "The Earth God's Shrine",
            "A small roadside shrine to the local earth god. The statue's face "
            + "has been scraped away. On the offering table lies a ledger: every "
            + "family of the village, each name with a little lantern drawn "
            + "beside it. All the lanterns are inked in red except the last "
            + "line, which is empty. The newest name, still wet, is yours. A "
            + "rough map on the back page marks a courtyard at the village heart."));

        g.addScene(new NormalScene("COURTYARD", "The Last Lantern",
            "At the heart of the village is a courtyard with a single pole, and "
            + "on it one lantern that has never been lit. Around you, the red "
            + "lanterns of every house have begun to flicker. From beyond the "
            + "walls comes the voice you heard on the road, closer now, saying "
            + "your name the way you would say the name of someone you have "
            + "missed for a very long time."));

        // ENDINGS: EndingScene objects. They get no outgoing edges (out-degree 0).
        g.addScene(new EndingScene("ENDING_TABLE", "A Place at the Table",
            "You sit. The rice is warm. Seven faces you do not remember smile at "
            + "you as though you have been away for years. Outside, a new red "
            + "lantern flickers to life beside the door, and the house begins, "
            + "slowly, to cast a shadow again.", "STRANGE"));

        g.addScene(new EndingScene("ENDING_WELL", "What the Well Keeps",
            "You lower your lantern. A small cold hand closes around the rope, "
            + "then around your wrist. The last thing you see is the light "
            + "sinking past you, illuminating dozens of faces in the water, all "
            + "of them looking up and waiting.", "BAD"));

        g.addScene(new EndingScene("ENDING_DAWN", "The Lantern Is Lit",
            "You write your own name on the blank tablet, hang it under the "
            + "lantern and light it. Every lantern in the village goes out at "
            + "once, and in the dark you hear doors opening, footsteps, voices "
            + "greeting one another. When dawn comes you are standing on the "
            + "road alone. The tablet in your hand is warm.", "GOOD"));

        g.addScene(new EndingScene("ENDING_FORGOTTEN", "Forgotten",
            "You leave the lantern dark and walk back to the gate. The road has "
            + "returned. You walk until sunrise and reach a town where no one "
            + "recognises you, not even, when you finally call home, your own "
            + "family. Behind you, very far away, a village is still waiting.", "BAD"));

        // This scene is written but NO edge leads to it on purpose.
        // The "Check story" option uses BFS to catch it as UNREACHABLE.
        g.addScene(new NormalScene("BELL_TOWER", "The Silent Bell Tower",
            "A bell tower leans over the rooftops. Its bell has no clapper, yet "
            + "the rope is worn smooth, as if someone rings it every night."));

        // ---------------- EDGES (choices) ----------------
        // addChoice(from, to, text shown to the player)

        g.addChoice("GATE", "SHADOWLESS_HOUSE", "Open the door of the house with no shadow");
        g.addChoice("GATE", "WELL", "Follow the sound of crying");
        g.addChoice("GATE", "SHRINE", "Climb the hill to the shrine");

        g.addChoice("SHADOWLESS_HOUSE", "ANCESTOR_ALTAR", "Examine the altar at the back of the room");
        g.addChoice("SHADOWLESS_HOUSE", "ENDING_TABLE", "Sit down at the eighth bowl");
        g.addChoice("SHADOWLESS_HOUSE", "GATE", "Back out slowly into the street");

        g.addChoice("ANCESTOR_ALTAR", "COURTYARD", "Take the tablet and look for the last lantern");
        g.addChoice("ANCESTOR_ALTAR", "GATE", "Return to the gate");

        g.addChoice("WELL", "ENDING_WELL", "Lower your lantern into the well");
        g.addChoice("WELL", "WELL_NAME", "Ask who is down there");
        g.addChoice("WELL", "GATE", "Walk away from the well");

        g.addChoice("WELL_NAME", "SHRINE", "Run to the shrine on the hill");
        g.addChoice("WELL_NAME", "COURTYARD", "Promise to come back, and go find a lantern");

        g.addChoice("SHRINE", "COURTYARD", "Follow the map to the courtyard");
        g.addChoice("SHRINE", "GATE", "Leave the ledger and go back down");

        // A LOCKED edge: only usable if the player carries the Blank Spirit Tablet.
        g.addChoice("COURTYARD", "ENDING_DAWN", "Write your name on the tablet and light the lantern",
                    "Blank Spirit Tablet");
        g.addChoice("COURTYARD", "ENDING_FORGOTTEN", "Leave it dark and walk out of the village");
        g.addChoice("COURTYARD", "GATE", "Go back to the gate");

        g.addChoice("BELL_TOWER", "COURTYARD", "Climb down toward the courtyard");

        return g;
    }
}

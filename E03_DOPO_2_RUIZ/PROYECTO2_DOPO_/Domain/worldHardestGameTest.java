package Domain;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias para la clase worldHardestGame.
 * Se prueban las funciones esenciales del controlador principal del juego.
 */
public class worldHardestGameTest {

    /**
     * constructor 
     */

    @Test
    public void constructorPlayerModeShouldCreateLevelOneAndPlayerOne() {
        worldHardestGame game = new worldHardestGame(
            worldHardestGame.GameMode.PLAYER,
            Player.Skin.ROJO
        );

        assertEquals(worldHardestGame.GameMode.PLAYER, game.getMode());
        assertNotNull(game.getPlayer1());
        assertNull(game.getPlayer2());
        assertEquals(1, game.getCurrentLevelNum());
        assertNotNull(game.getCurrentLevel());
    }

    @Test
    public void constructorPvmShouldCreateMachineAsPlayerTwo() {
        worldHardestGame game = new worldHardestGame(Player.Skin.AZUL);

        assertEquals(worldHardestGame.GameMode.PVM, game.getMode());
        assertNotNull(game.getPlayer1());
        assertNotNull(game.getPlayer2());
        assertEquals(Player.Skin.AZUL, game.getPlayer1().getOriginalSkin());
        assertEquals(Player.Skin.ROJO, game.getPlayer2().getOriginalSkin());
    }

    /**
     * movePlayer
     */

    @Test
    public void movePlayerShouldMovePlayerOneWhenGameIsActive() {
        worldHardestGame game = new worldHardestGame(
            worldHardestGame.GameMode.PLAYER,
            Player.Skin.ROJO
        );

        int initialX = game.getPlayer1().getCol();
        int initialY = game.getPlayer1().getRow();

        game.movePlayer(1, 0);

        assertEquals(initialX + 1, game.getPlayer1().getCol());
        assertEquals(initialY, game.getPlayer1().getRow());
    }

    @Test
    public void movePlayerShouldNotMoveWhenGameIsPaused() {
        worldHardestGame game = new worldHardestGame(
            worldHardestGame.GameMode.PLAYER,
            Player.Skin.ROJO
        );

        int initialX = game.getPlayer1().getCol();
        int initialY = game.getPlayer1().getRow();

        game.togglePause();
        game.movePlayer(1, 0);

        assertEquals(initialX, game.getPlayer1().getCol());
        assertEquals(initialY, game.getPlayer1().getRow());
    }

    /**
     * moveMachine
     */

    @Test
    public void moveMachineShouldNotCreatePlayerTwoInPlayerMode() {
        worldHardestGame game = new worldHardestGame(
            worldHardestGame.GameMode.PLAYER,
            Player.Skin.ROJO
        );

        assertNull(game.getPlayer2());

        game.moveMachine();

        assertNull(game.getPlayer2());
    }

    @Test
    public void moveMachineShouldKeepMachineInsideBoardInPvmMode() {
        worldHardestGame game = new worldHardestGame(Player.Skin.ROJO);

        for (int i = 0; i < 50; i++) {
            game.moveMachine();

            int machineX = game.getPlayer2().getCol();
            int machineY = game.getPlayer2().getRow();

            assertTrue(machineX >= 0);
            assertTrue(machineX < game.getCols());
            assertTrue(machineY >= 0);
            assertTrue(machineY < game.getRows());
        }
    }

    /**
     * checkCollisions
     */

    @Test
    public void checkCollisionsShouldCollectCoinWhenPlayerIsOnCoin() {
        worldHardestGame game = new worldHardestGame(
            worldHardestGame.GameMode.PLAYER,
            Player.Skin.ROJO
        );

        Player player = game.getPlayer1();
        Coin firstCoin = game.getCurrentLevel().getCoins().get(0);

        player.setX(firstCoin.getCol());
        player.setY(firstCoin.getRow());

        assertEquals(0, player.getCoinsCollected());
        assertFalse(firstCoin.isCollected());

        game.checkCollisions();

        assertEquals(1, player.getCoinsCollected());
        assertTrue(firstCoin.isCollected());
    }

    @Test
    public void checkCollisionsShouldRespawnPlayerWhenTouchingEnemy() {
        worldHardestGame game = new worldHardestGame(
            worldHardestGame.GameMode.PLAYER,
            Player.Skin.ROJO
        );

        Player player = game.getPlayer1();
        Enemy enemy = game.getCurrentLevel().getEnemies().get(0);

        int startX = player.getStartX();
        int startY = player.getStartY();

        player.setX(enemy.getCol());
        player.setY(enemy.getRow());

        assertEquals(0, player.getDeaths());

        game.checkCollisions();

        assertEquals(1, player.getDeaths());
        assertEquals(startX, player.getCol());
        assertEquals(startY, player.getRow());
    }

    /**
     * tick
     */

    @Test
    public void tickShouldDecreaseTimeByOneWhenGameIsActive() {
        worldHardestGame game = new worldHardestGame(
            worldHardestGame.GameMode.PLAYER,
            Player.Skin.ROJO
        );

        int initialTime = game.getTimeRemaining();

        boolean timeExpired = game.tick();

        assertFalse(timeExpired);
        assertEquals(initialTime - 1, game.getTimeRemaining());
    }

    @Test
    public void tickShouldNotDecreaseTimeWhenPaused() {
        worldHardestGame game = new worldHardestGame(
            worldHardestGame.GameMode.PLAYER,
            Player.Skin.ROJO
        );

        int initialTime = game.getTimeRemaining();

        game.togglePause();
        boolean timeExpired = game.tick();

        assertFalse(timeExpired);
        assertEquals(initialTime, game.getTimeRemaining());
    }

    /**
     *pause
     */
    @Test
    public void togglePauseShouldPauseGame() {
        worldHardestGame game = new worldHardestGame(
            worldHardestGame.GameMode.PLAYER,
            Player.Skin.ROJO
        );

        assertFalse(game.isPaused());

        game.togglePause();

        assertTrue(game.isPaused());
    }

    @Test
    public void togglePauseShouldResumeGameWhenCalledTwice() {
        worldHardestGame game = new worldHardestGame(
            worldHardestGame.GameMode.PLAYER,
            Player.Skin.ROJO
        );

        game.togglePause();
        game.togglePause();

        assertFalse(game.isPaused());
    }
    /**
     * nextLevel()
     */

    @Test
    public void nextLevelShouldReturnFalseWhenOnlyLevelOneExists() {
        worldHardestGame game = new worldHardestGame(
            worldHardestGame.GameMode.PLAYER,
            Player.Skin.ROJO
        );

        boolean result = game.nextLevel();

        assertFalse(result);
        assertEquals(1, game.getCurrentLevelNum());
    }

    @Test
    public void nextLevelShouldNotChangeCurrentLevelWhenNoNextLevelExists() {
        worldHardestGame game = new worldHardestGame(
            worldHardestGame.GameMode.PLAYER,
            Player.Skin.ROJO
        );

        Level originalLevel = game.getCurrentLevel();

        game.nextLevel();

        assertSame(originalLevel, game.getCurrentLevel());
        assertEquals(1, game.getCurrentLevelNum());
    }

    /**
     * saveGame() y loadGame()
     */
    @Test
    public void saveAndLoadGameShouldPreserveBasicState() throws DOPOException {
        worldHardestGame game = new worldHardestGame(
            worldHardestGame.GameMode.PLAYER,
            Player.Skin.VERDE
        );

        game.movePlayer(1, 0);
        game.tick();

        File tempFile = new File("test_save_game.dat");

        try {
            game.saveGame(tempFile.getAbsolutePath());

            worldHardestGame loaded = worldHardestGame.loadGame(tempFile.getAbsolutePath());

            assertEquals(game.getMode(), loaded.getMode());
            assertEquals(game.getCurrentLevelNum(), loaded.getCurrentLevelNum());
            assertEquals(game.getTimeRemaining(), loaded.getTimeRemaining());
            assertEquals(game.getPlayer1().getCol(), loaded.getPlayer1().getCol());
            assertEquals(game.getPlayer1().getRow(), loaded.getPlayer1().getRow());
        } finally {
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    @Test
    public void loadGameShouldThrowExceptionWhenFileDoesNotExist() {
        assertThrows(DOPOException.class, new org.junit.jupiter.api.function.Executable() {
            public void execute() throws Throwable {
                worldHardestGame.loadGame("archivo_que_no_existe.dat");
            }
        });
    }
}
package ru.alexandrstal.hoursetask;

import java.util.*;

public class App {

    /**
     * Клетки доски пронумерованы числами от 1 до 9
     * Position хранит состояние доски с фигурами: список клеток занятых белыми и черными конями и чей сейчас ход
     * Две позиции считаются одинаковыми если черные и белые фигуры стоят на тех же позициях
     */

    static class Position {
        // ход белых или черных?
        private boolean whiteTurn;
        // положение белых
        private List<Short> whiteFigs;
        // положение черных
        private List<Short> blackFigs;

        public boolean isWhiteTurn() {
            return whiteTurn;
        }

        public void setWhiteTurn(boolean whiteTurn) {
            this.whiteTurn = whiteTurn;
        }

        public List<Short> getWhiteFigs() {
            return whiteFigs;
        }

        public void setWhiteFigs(List<Short> whiteFigs) {
            this.whiteFigs = whiteFigs;
        }

        public List<Short> getBlackFigs() {
            return blackFigs;
        }

        public void setBlackFigs(List<Short> blackFigs) {
            this.blackFigs = blackFigs;
        }

        @Override
        public String toString() {
            return "Position{" +
                    "whiteTurn=" + whiteTurn +
                    ", whiteFigs=" + whiteFigs +
                    ", blackFigs=" + blackFigs +
                    '}';
        }

        /**
         * Две позиции считаются одинаковыми если черные и белые фигуры стоят на тех же позициях
         * @param o
         * @return
         */

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Position position = (Position) o;

            if (!whiteFigs.containsAll(position.whiteFigs)) return false;
            return blackFigs.containsAll(position.blackFigs);
        }

        @Override
        public int hashCode() {
            return 1;
        }
    }


    /**
     *
     * метод возвращает все возможные позиции в которые можно перейти из текущей позиции current
     */
    static Set<Position> getAviablePositions(Position current) {
        Set<Position> positions = new HashSet<>();

        List<Short> okk = new ArrayList<>();
        okk.addAll(current.getWhiteFigs());
        okk.addAll(current.getBlackFigs()); // список клеток на которых стоят фигуры

        // фигуры которыми можно ходить на данном ходу
        List<Short> activeFigs = current.isWhiteTurn() ? current.getWhiteFigs() : current.getBlackFigs();

        for (Short currentFig : activeFigs) {
            List<Short> aviables = avlableMoves.get(currentFig); // доступые клетки для данной фигуры
            for (Short aviable : aviables) {
                if (!okk.contains(aviable)) { // если на этой клетке нет фигуры - это доступная позиция

                    // новое положение фигур
                    List<Short> newFigPositions = new ArrayList<Short>(activeFigs);
                    newFigPositions.remove(currentFig);
                    newFigPositions.add(aviable);

                    Position position = new Position(); // новая позиция
                    position.setWhiteTurn(!current.isWhiteTurn()); // следующий ход - другого цвета
                    if (current.isWhiteTurn()) {
                        position.setWhiteFigs(newFigPositions);
                        position.setBlackFigs(current.getBlackFigs());
                    } else {
                        position.setWhiteFigs(current.getWhiteFigs());
                        position.setBlackFigs(newFigPositions);
                    }
                    positions.add(position); // сохраняем позицию
                }
            }
        }
        return positions;
    }

    /**
     *  Для набора позиций находим новый набор - позиций в которые можно перейти из этого набора
     */
    static Set<Position> iterate(Set<Position> positions){
        Set<Position> newIteration = new HashSet<>();
        System.out.println(positions.size());
        for(Position cp : positions){
            System.out.println(cp);
            Set<Position> av = getAviablePositions(cp);
            for (Position p:av){
                if (p.equals(finPosition)){  // если позиция = искомой - пишем об этом и выходим!
                    System.out.println("ПЕРЕСТАНОВКА ЗАКОНЧЕНА!");
                    return null;
                }

                if (!oldPositions.contains(p)){ // если мы уже были в этой позиции - отбрасываем ее, так как
                    // хождение назад не имеет смысла, в противном случае - запоминаем позицию
                    oldPositions.add(p);
                    newIteration.add(p);
                }
            }

        }
        return newIteration;
    }

    /**
     * Для каждой клетки - набор клеток на которые с нее может уйти конь
     */
    static Map<Short, List<Short>> avlableMoves = new HashMap<>();

    /**
     * Искомая позиция
     */
    static Position finPosition;

    /**
     * В эту переменную пишем просмотренные позиции чтобы не проверять их повторно -
     */
    static Set<Position> oldPositions= new HashSet<>();

    /**
     * Инициализуем стат. переменные
     */

    static {avlableMoves.put((short) 1, Arrays.asList(new Short[]{6, 8}));
        avlableMoves.put((short) 2, Arrays.asList(new Short[]{7, 9}));
        avlableMoves.put((short) 3, Arrays.asList(new Short[]{4, 8}));
        avlableMoves.put((short) 4, Arrays.asList(new Short[]{3, 9}));
        avlableMoves.put((short) 5, Arrays.asList(new Short[]{}));
        avlableMoves.put((short) 6, Arrays.asList(new Short[]{1, 7}));
        avlableMoves.put((short) 7, Arrays.asList(new Short[]{2, 6}));
        avlableMoves.put((short) 8, Arrays.asList(new Short[]{1, 3}));
        avlableMoves.put((short) 9, Arrays.asList(new Short[]{2, 4}));

        finPosition = new Position();
        finPosition.setWhiteTurn(true);
        finPosition.setWhiteFigs(Arrays.asList(new Short[]{7, 9}));
        finPosition.setBlackFigs(Arrays.asList(new Short[]{3, 1}));

    }


    public static void main(String[] args) {

        /**
         * стартовая позиция
         */
        Position startPosition = new Position();
        startPosition.setWhiteTurn(true);
        startPosition.setWhiteFigs(Arrays.asList(new Short[]{1, 3}));
        startPosition.setBlackFigs(Arrays.asList(new Short[]{7, 9}));

        Set<Position> positions = getAviablePositions(startPosition);

        //ходим ло тех пор пока есть доступные ходы или не попали в финальную позицию
        int turn = 1;
        while ((positions!=null)&&!positions.isEmpty()){
            turn++;
            System.out.println("Turn" + turn );
            positions = iterate(positions);
        }

    }


}

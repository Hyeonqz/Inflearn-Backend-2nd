package com.readable.code.minesweeper.board.cell;

import java.util.Objects;

public class CellSnapshot {
	private final CellSnapshotStatus status;
	private final int nearbyLandMineCount;

	public CellSnapshot (CellSnapshotStatus status, int nearbyLandMineCount) {
		this.status = status;
		this.nearbyLandMineCount = nearbyLandMineCount;
	}

	public static CellSnapshot of(CellSnapshotStatus cellSnapshotStatus, int nearbyLandMineCount) {
		return new CellSnapshot(cellSnapshotStatus, nearbyLandMineCount);
	}

	public static CellSnapshot ofEmpty() {
		return of(com.readable.code.minesweeper.board.cell.CellSnapshotStatus.EMPTY, 0);
	}

	public static CellSnapshot ofFlag() {
		return of(com.readable.code.minesweeper.board.cell.CellSnapshotStatus.FLAG,0);
	}

	public static CellSnapshot ofLandmine() {
		return of(com.readable.code.minesweeper.board.cell.CellSnapshotStatus.LAND_MINE,0);
	}

	public static CellSnapshot ofNumber(int nearbyLandMineCount) {
		return of(com.readable.code.minesweeper.board.cell.CellSnapshotStatus.NUMBER,nearbyLandMineCount);
	}
	public static CellSnapshot ofUnchecked() {
		return of(com.readable.code.minesweeper.board.cell.CellSnapshotStatus.UNCHECKED,0);
	}

	public CellSnapshotStatus getStatus () {
		return status;
	}

	public boolean isSameStatus (CellSnapshotStatus cellSnapshotStatus) {
		return this.status == cellSnapshotStatus;
	}

	public int getNearbyLandMineCount () {
		return nearbyLandMineCount;
	}

	// Value Object 라는 생각이 들면 equals & hashCode 를 재정의 하자
	@Override
	public boolean equals (Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		CellSnapshot that = (CellSnapshot)o;
		return nearbyLandMineCount == that.nearbyLandMineCount && status == that.status;
	}

	@Override
	public int hashCode () {
		return Objects.hash(status, nearbyLandMineCount);
	}

}

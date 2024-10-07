package com.readable.code.minesweeper.io.sign.enums;

import java.util.Arrays;

import com.readable.code.minesweeper.cell.CellSnapshot;
import com.readable.code.minesweeper.cell.CellSnapshotStatus;
import com.readable.code.minesweeper.io.sign.CellSignProvidable;

public enum CellSignProvider implements CellSignProvidable {
	EMPTY(CellSnapshotStatus.EMPTY) {
		@Override
		public String provide (CellSnapshot cellSnapshot) {
			return EMPTY_SIGN;
		}
	},
	FLAG(CellSnapshotStatus.FLAG) {
		@Override
		public String provide (CellSnapshot cellSnapshot) {
			return FLAG_SIGN;
		}
	},
	LAND_MINE(CellSnapshotStatus.LAND_MINE) {
		@Override
		public String provide (CellSnapshot cellSnapshot) {
			return LAND_MINE_SIGN;
		}
	},
	NUMBER(CellSnapshotStatus.NUMBER) {
		@Override
		public String provide (CellSnapshot cellSnapshot) {
			return String.valueOf(cellSnapshot.getNearbyLandMineCount());
		}
	},
	UNCHECKED(CellSnapshotStatus.UNCHECKED) {
		@Override
		public String provide (CellSnapshot cellSnapshot) {
			return UNCHECKED_SIGN;
		}
	}
	;

	private static final String EMPTY_SIGN = "◼";
	private static final String FLAG_SIGN = "⚑";
	private static final String LAND_MINE_SIGN = "*";
	private static final String UNCHECKED_SIGN = "□";

	private final CellSnapshotStatus status;

	CellSignProvider (CellSnapshotStatus cellSnapshotStatus) {
		this.status = cellSnapshotStatus;
	}

	@Override
	public String provide (CellSnapshot cellSnapshot) {
		return "";
	}

	@Override
	public boolean supports (CellSnapshot cellSnapshot) {
		return cellSnapshot.isSameStatus(status);
	}

	public static String findCellSignFrom(CellSnapshot cellSnapshot) {
		CellSignProvider cellSignProvider = findBy(cellSnapshot);
		return cellSignProvider.provide(cellSnapshot);
	}

	private static CellSignProvider findBy (CellSnapshot cellSnapshot) {
		return Arrays.stream(values())
			.filter(provider -> provider.supports(cellSnapshot))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("확인할 수 없는 셀 입니다."));
	}
}

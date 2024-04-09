package persistentlayer;

import java.util.Map;

import customexceptions.CustomException;
import customexceptions.InvalidValueException;
import model.Branch;
import utility.ActiveStatus;

public interface BranchManager {

	int addBranch(Branch branch) throws CustomException;

	Branch getBranch(int branchId) throws CustomException, InvalidValueException;

	void removeBranch(int branchId, int modifiedBy, long modifiedOn) throws CustomException;

	boolean isValidBranch(int branchId) throws InvalidValueException, CustomException;

	Map<Integer, Branch> getBranches(ActiveStatus status) throws CustomException;
}

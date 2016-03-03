import sys
import math

data = [[0, 0, 1], [1, 0, 0], [1, 1, 0]]

a = 0.15

def get_outlink(matrix, index):
	M = len(matrix)
	count = 0
	for i in range(M): 
		if matrix[i][index] == 1:
			count += 1
	return count
	
def page_rank(matrix, count, ranking):
	S = len(matrix)
	ep = a / S
	newranking = [0.0 for x in range(S)]

	for i in range(S): # row
		for j in range(S): # col
			if matrix[i][j] == 1:
				newranking[i] += ranking[j] / get_outlink(matrix, j)
		newranking[i] += ep

	print 'iteration {0}:\nbefore norm:'.format(count + 1), ' '.join(str(format(x, '.2f')) for x in newranking)
	
	after_norm = [1.0/sum(newranking) * x for x in newranking]
	print 'after norm: ', ' '.join(str(format(x, '.2f')) for x in after_norm)
	print ''
	
	count += 1
	if count >= 3:
		return
	else:
		page_rank(matrix, count, after_norm)

print 'Calculate page rank:'
ranking = [1.0 / len(data) for x in range(len(data))]
page_rank(data, 0, ranking)